package SPITEC.Prestamo.Service;

import SPITEC.DetallePrestamo.Entity.DetalleEntity;
import SPITEC.DetallePrestamo.Repository.DetalleRepository;
import SPITEC.EstadoPrestamo.Repository.EstadoRepository;
import SPITEC.Exception.AppException;
import SPITEC.Inventario.Entity.InventarioEntity;
import SPITEC.Inventario.Repository.InventarioRepository;
import SPITEC.Materiales.Repository.MaterialesRepository;
import SPITEC.Prestamo.DTO.PrestamoDTO;
import SPITEC.Prestamo.Entity.PrestamoEntity;
import SPITEC.Prestamo.Repository.PrestamoRepository;
import SPITEC.Roles.Repository.RolesRepository;
import SPITEC.Usuarios.Entity.UsuariosEntity;
import SPITEC.Usuarios.Repository.UsuariosRepository;
import SPITEC.Utils.TextUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PrestamoService {
    private final PrestamoRepository repo;
    private final DetalleRepository detalleRepository;
    private final InventarioRepository inventarioRepository;
    private final UsuariosRepository usuariosRepository;
    private final MaterialesRepository materialesRepository;
    private final EstadoRepository estadoRepository;
    private final RolesRepository rolesRepository;

    public PrestamoService(PrestamoRepository repo, DetalleRepository detalleRepository, InventarioRepository inventarioRepository, UsuariosRepository usuariosRepository, MaterialesRepository materialesRepository, EstadoRepository estadoRepository, RolesRepository rolesRepository) {
        this.repo = repo;
        this.detalleRepository = detalleRepository;
        this.inventarioRepository = inventarioRepository;
        this.usuariosRepository = usuariosRepository;
        this.materialesRepository = materialesRepository;
        this.estadoRepository = estadoRepository;
        this.rolesRepository = rolesRepository;
    }

    public List<PrestamoDTO> getLoans(UsuariosEntity actor) {
        List<PrestamoEntity> loans = isEmployee(actor) ? repo.findByIdUsuario(actor.getId()) : repo.findAll();
        return loans.stream().filter(item -> List.of("PENDIENTE", "APROBADO", "RECHAZADO").contains(getStateName(item.getEstado()))).map(this::toDTO).toList();
    }

    public PrestamoDTO createLoanRequest(UsuariosEntity actor, PrestamoDTO dto) {
        Long inventoryId = dto.getInventoryId();
        String start = TextUtils.cleanText(dto.getStartDate()).isEmpty() ? LocalDate.now().toString() : dto.getStartDate();
        String end = TextUtils.cleanText(dto.getEndDate());
        Long userId = canManageLoans(actor) && dto.getUserId() != null ? dto.getUserId() : actor.getId();
        if (inventoryId == null || start.isEmpty() || end.isEmpty()) throw new AppException("Herramienta, fecha de inicio y fecha de entrega son obligatorias.", 400);
        if (!LocalDate.parse(end).isAfter(LocalDate.parse(start))) throw new AppException("La fecha de entrega debe ser posterior a la fecha de inicio.", 400);
        if (LocalDate.parse(end).isAfter(LocalDate.parse(start).plusMonths(1))) throw new AppException("El prestamo no puede durar mas de 1 mes.", 400);
        if (!usuariosRepository.existsById(userId)) throw new AppException("Usuario solicitante no encontrado.", 404);
        InventarioEntity item = inventarioRepository.findById(inventoryId).orElseThrow(() -> new AppException("Herramienta no encontrada.", 404));
        if (!"Disponible".equals(item.getEstado())) throw new AppException("La herramienta no esta disponible para prestamo.", 409);
        PrestamoEntity loan = new PrestamoEntity();
        loan.setIdUsuario(userId);
        loan.setFechaInicio(start);
        loan.setFechaFin(end);
        loan.setEstado(getStateId("PENDIENTE"));
        loan.setCreadoEn(LocalDateTime.now().toString());
        PrestamoEntity saved = repo.save(loan);
        DetalleEntity detail = new DetalleEntity();
        detail.setIdPrestamo(saved.getId());
        detail.setIdInventario(inventoryId);
        detail.setCantidad(1);
        detalleRepository.save(detail);
        return toDTO(saved);
    }

    public PrestamoDTO updateLoanState(Long id, String state) {
        String normalized = TextUtils.cleanText(state).toUpperCase();
        if (!List.of("APROBADO", "RECHAZADO", "PENDIENTE").contains(normalized)) throw new AppException("Estado de prestamo no valido.", 400);
        PrestamoEntity loan = repo.findById(id).orElseThrow(() -> new AppException("Prestamo no encontrado.", 404));
        DetalleEntity detail = detalleRepository.findFirstByIdPrestamo(id).orElseThrow(() -> new AppException("Prestamo no encontrado.", 404));
        loan.setEstado(getStateId(normalized));
        repo.save(loan);
        inventarioRepository.findById(detail.getIdInventario()).ifPresent(item -> {
            if ("APROBADO".equals(normalized)) item.setEstado("Prestado");
            if ("RECHAZADO".equals(normalized) && "Prestado".equals(item.getEstado())) item.setEstado("Disponible");
            inventarioRepository.save(item);
        });
        return toDTO(loan);
    }

    public boolean canManageLoans(UsuariosEntity user) {
        return "ADMINISTRADOR".equals(getRole(user));
    }

    public boolean canViewOperationalRecords(UsuariosEntity user) {
        return !"IT".equals(getRole(user));
    }

    public PrestamoDTO toDTO(PrestamoEntity loan) {
        DetalleEntity detail = detalleRepository.findFirstByIdPrestamo(loan.getId()).orElse(null);
        InventarioEntity inventory = detail == null ? null : inventarioRepository.findById(detail.getIdInventario()).orElse(null);
        String item = inventory == null ? "" : materialesRepository.findById(inventory.getIdMaterial()).map(material -> material.getNombreMaterial()).orElse("");
        UsuariosEntity user = usuariosRepository.findById(loan.getIdUsuario()).orElse(null);
        PrestamoDTO dto = new PrestamoDTO();
        dto.setId(loan.getId());
        dto.setCode(String.format("PR-%03d", loan.getId()));
        dto.setStudent(user == null ? "" : user.getNombreUsuario() + " " + user.getApellidoUsuario());
        dto.setDate(loan.getFechaInicio());
        dto.setItem(item);
        dto.setState(formatState(getStateName(loan.getEstado())));
        return dto;
    }

    private String getRole(UsuariosEntity user) {
        return rolesRepository.findById(user.getIdRol()).map(item -> item.getNombreRol()).orElse("");
    }

    private boolean isEmployee(UsuariosEntity user) {
        return "EMPLEADO".equals(getRole(user));
    }

    private Long getStateId(String name) {
        return estadoRepository.findByNombreEstado(name).orElseThrow(() -> new AppException("Estado de prestamo no valido.", 400)).getId();
    }

    private String getStateName(Long id) {
        return estadoRepository.findById(id).map(item -> item.getNombreEstado()).orElse("");
    }

    private String formatState(String state) {
        return switch (state) {
            case "PENDIENTE" -> "Pendiente";
            case "APROBADO" -> "Aprobado";
            case "RECHAZADO" -> "Rechazado";
            case "DEVUELTO" -> "Devuelto";
            case "REVISION" -> "Revision";
            case "RETRASADO" -> "Retrasado";
            default -> state;
        };
    }
}
