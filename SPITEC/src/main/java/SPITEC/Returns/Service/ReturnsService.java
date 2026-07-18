package SPITEC.Returns.Service;

import SPITEC.DetallePrestamo.Entity.DetalleEntity;
import SPITEC.DetallePrestamo.Repository.DetalleRepository;
import SPITEC.EstadoPrestamo.Repository.EstadoRepository;
import SPITEC.Exception.AppException;
import SPITEC.Historial.Entity.HistorialEntity;
import SPITEC.Historial.Repository.HistorialRepository;
import SPITEC.Inventario.Entity.InventarioEntity;
import SPITEC.Inventario.Repository.InventarioRepository;
import SPITEC.Materiales.Entity.MaterialesEntity;
import SPITEC.Materiales.Repository.MaterialesRepository;
import SPITEC.Prestamo.Entity.PrestamoEntity;
import SPITEC.Prestamo.Repository.PrestamoRepository;
import SPITEC.Reportes.Service.ReportesService;
import SPITEC.Returns.DTO.ReturnsDTO;
import SPITEC.Roles.Repository.RolesRepository;
import SPITEC.Usuarios.Entity.UsuariosEntity;
import SPITEC.Usuarios.Repository.UsuariosRepository;
import SPITEC.Utils.TextUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReturnsService {
    private final PrestamoRepository prestamoRepository;
    private final DetalleRepository detalleRepository;
    private final InventarioRepository inventarioRepository;
    private final MaterialesRepository materialesRepository;
    private final UsuariosRepository usuariosRepository;
    private final EstadoRepository estadoRepository;
    private final RolesRepository rolesRepository;
    private final HistorialRepository historialRepository;
    private final ReportesService reportesService;

    public ReturnsService(PrestamoRepository prestamoRepository, DetalleRepository detalleRepository, InventarioRepository inventarioRepository, MaterialesRepository materialesRepository, UsuariosRepository usuariosRepository, EstadoRepository estadoRepository, RolesRepository rolesRepository, HistorialRepository historialRepository, ReportesService reportesService) {
        this.prestamoRepository = prestamoRepository;
        this.detalleRepository = detalleRepository;
        this.inventarioRepository = inventarioRepository;
        this.materialesRepository = materialesRepository;
        this.usuariosRepository = usuariosRepository;
        this.estadoRepository = estadoRepository;
        this.rolesRepository = rolesRepository;
        this.historialRepository = historialRepository;
        this.reportesService = reportesService;
    }

    public List<ReturnsDTO> getReturns(UsuariosEntity actor) {
        return prestamoRepository.findAll().stream()
                .filter(item -> !isEmployee(actor) || item.getIdUsuario().equals(actor.getId()))
                .filter(item -> List.of("APROBADO", "REVISION", "RETRASADO").contains(getStateName(item.getEstado())))
                .map(this::toDTO).toList();
    }

    public Object updateReturn(Long id, ReturnsDTO body, UsuariosEntity actor) {
        PrestamoEntity loan = prestamoRepository.findById(id).orElseThrow(() -> new AppException("Devolucion no encontrada.", 404));
        DetalleEntity detail = detalleRepository.findFirstByIdPrestamo(id).orElseThrow(() -> new AppException("Devolucion no encontrada.", 404));
        InventarioEntity inventory = inventarioRepository.findById(detail.getIdInventario()).orElseThrow(() -> new AppException("Devolucion no encontrada.", 404));
        String action = TextUtils.cleanText(body.getAction()).toLowerCase();
        if ("returned".equals(action) || "devuelto".equals(action)) {
            if (!canManageReturns(actor)) throw new AppException("Solo Administrador o IT puede registrar devoluciones.", 403);
            loan.setEstado(getStateId("DEVUELTO"));
            inventory.setEstado("Disponible");
            prestamoRepository.save(loan);
            inventarioRepository.save(inventory);
            saveHistory(loan, inventory, "Devuelto");
            return java.util.Map.of("ok", true, "removed", true);
        }
        if ("damage".equals(action) || "danio".equals(action) || "daño".equals(action)) {
            if (!canManageLoans(actor) && !loan.getIdUsuario().equals(actor.getId())) throw new AppException("Solo puedes reportar danos de tus propios prestamos.", 403);
            loan.setEstado(getStateId("REVISION"));
            inventory.setEstado("Daniado");
            prestamoRepository.save(loan);
            inventarioRepository.save(inventory);
            reportesService.createDamageReport(loan, inventory, TextUtils.cleanText(body.getDamageDescription()), actor.getId());
            return toDTO(loan);
        }
        throw new AppException("Accion no valida.", 400);
    }

    public boolean canViewOperationalRecords(UsuariosEntity user) {
        return !"IT".equals(getRole(user));
    }

    private void saveHistory(PrestamoEntity loan, InventarioEntity inventory, String status) {
        MaterialesEntity material = materialesRepository.findById(inventory.getIdMaterial()).orElse(null);
        UsuariosEntity user = usuariosRepository.findById(loan.getIdUsuario()).orElse(null);
        HistorialEntity entity = new HistorialEntity();
        entity.setIdInventario(inventory.getId());
        entity.setIdUsuario(loan.getIdUsuario());
        entity.setCodigoInventario(inventory.getCodigoInventario());
        entity.setNombreMaterial(material == null ? "" : material.getNombreMaterial());
        entity.setFechaInicio(loan.getFechaInicio());
        entity.setFechaFin(LocalDate.now().toString());
        entity.setNombreUsuario(user == null ? "" : user.getNombreUsuario() + " " + user.getApellidoUsuario());
        entity.setEstado(status);
        entity.setCreadoEn(LocalDate.now().toString());
        historialRepository.save(entity);
    }

    private ReturnsDTO toDTO(PrestamoEntity loan) {
        DetalleEntity detail = detalleRepository.findFirstByIdPrestamo(loan.getId()).orElse(null);
        InventarioEntity inventory = detail == null ? null : inventarioRepository.findById(detail.getIdInventario()).orElse(null);
        MaterialesEntity material = inventory == null ? null : materialesRepository.findById(inventory.getIdMaterial()).orElse(null);
        UsuariosEntity user = usuariosRepository.findById(loan.getIdUsuario()).orElse(null);
        ReturnsDTO dto = new ReturnsDTO();
        dto.setId(loan.getId());
        dto.setStudent(user == null ? "" : user.getNombreUsuario() + " " + user.getApellidoUsuario());
        dto.setItem(material == null ? "" : material.getNombreMaterial());
        dto.setCode(inventory == null ? "" : inventory.getCodigoInventario());
        dto.setDueDate(loan.getFechaFin());
        String state = getStateName(loan.getEstado());
        dto.setStatus("APROBADO".equals(state) && loan.getFechaFin().compareTo(LocalDate.now().toString()) < 0 ? "Retrasado" : formatStatus(state));
        return dto;
    }

    private boolean canManageReturns(UsuariosEntity user) { return "ADMINISTRADOR".equals(getRole(user)); }
    private boolean canManageLoans(UsuariosEntity user) { return "ADMINISTRADOR".equals(getRole(user)); }
    private boolean isEmployee(UsuariosEntity user) { return "EMPLEADO".equals(getRole(user)); }
    private String getRole(UsuariosEntity user) { return rolesRepository.findById(user.getIdRol()).map(item -> item.getNombreRol()).orElse(""); }
    private Long getStateId(String name) { return estadoRepository.findByNombreEstado(name).orElseThrow(() -> new AppException("Estado no valido.", 400)).getId(); }
    private String getStateName(Long id) { return estadoRepository.findById(id).map(item -> item.getNombreEstado()).orElse(""); }
    private String formatStatus(String status) { return "REVISION".equals(status) ? "Revision" : "RETRASADO".equals(status) ? "Retrasado" : "En tiempo"; }
}
