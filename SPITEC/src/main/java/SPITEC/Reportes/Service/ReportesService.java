package SPITEC.Reportes.Service;

import SPITEC.Exception.AppException;
import SPITEC.Inventario.Entity.InventarioEntity;
import SPITEC.Materiales.Entity.MaterialesEntity;
import SPITEC.Materiales.Repository.MaterialesRepository;
import SPITEC.Prestamo.Entity.PrestamoEntity;
import SPITEC.Reportes.DTO.ReportesDTO;
import SPITEC.Reportes.Entity.ReportesEntity;
import SPITEC.Reportes.Repository.ReportesRepository;
import SPITEC.Roles.Repository.RolesRepository;
import SPITEC.Usuarios.Entity.UsuariosEntity;
import SPITEC.Usuarios.Repository.UsuariosRepository;
import SPITEC.Utils.TextUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportesService {
    private final ReportesRepository repo;
    private final UsuariosRepository usuariosRepository;
    private final RolesRepository rolesRepository;
    private final MaterialesRepository materialesRepository;

    public ReportesService(ReportesRepository repo, UsuariosRepository usuariosRepository, RolesRepository rolesRepository, MaterialesRepository materialesRepository) {
        this.repo = repo;
        this.usuariosRepository = usuariosRepository;
        this.rolesRepository = rolesRepository;
        this.materialesRepository = materialesRepository;
    }

    public List<ReportesDTO> getReports(UsuariosEntity actor) {
        List<ReportesEntity> data = isEmployee(actor) ? repo.findByGeneradoPorOrderByIdDesc(actor.getId()) : repo.findAllByOrderByIdDesc();
        return data.stream().map(this::toDTO).toList();
    }

    public ReportesDTO createReport(ReportesDTO dto, Long userId) {
        ReportesEntity entity = new ReportesEntity();
        entity.setTipoReporte(defaultText(dto.getType(), "General"));
        entity.setTitulo(defaultText(dto.getTitle(), "Reporte generado"));
        entity.setDescripcion(defaultText(dto.getDescription(), "Reporte generado desde SIPTEC."));
        entity.setContenido(defaultText(dto.getContent(), entity.getDescripcion()));
        entity.setGeneradoPor(userId);
        entity.setEstado("Generado");
        entity.setCreadoEn(LocalDateTime.now().toString());
        return toDTO(repo.save(entity));
    }

    public void createDamageReport(PrestamoEntity loan, InventarioEntity inventory, String description, Long reporterId) {
        MaterialesEntity material = materialesRepository.findById(inventory.getIdMaterial()).orElse(null);
        ReportesDTO dto = new ReportesDTO();
        dto.setType("Danio");
        dto.setTitle("Danio reportado: " + (material == null ? inventory.getCodigoInventario() : material.getNombreMaterial()));
        dto.setDescription(description.isEmpty() ? "Reporte de danio sin descripcion detallada." : description);
        dto.setContent("REPORTE DE DANIO\n\nCodigo: " + inventory.getCodigoInventario() + "\nHerramienta: " + (material == null ? "" : material.getNombreMaterial()) + "\nFecha de prestamo: " + loan.getFechaInicio() + "\n\nDanos presentados:\n" + dto.getDescription());
        createReport(dto, reporterId);
    }

    public void deleteReport(Long id, UsuariosEntity actor) {
        ReportesEntity report = repo.findById(id).orElseThrow(() -> new AppException("Reporte no encontrado.", 404));
        if (isEmployee(actor) && !actor.getId().equals(report.getGeneradoPor())) throw new AppException("Solo puedes eliminar tus propios reportes.", 403);
        repo.delete(report);
    }

    private ReportesDTO toDTO(ReportesEntity entity) {
        ReportesDTO dto = new ReportesDTO();
        dto.setId(entity.getId());
        dto.setType(entity.getTipoReporte());
        dto.setTitle(entity.getTitulo());
        dto.setDescription(entity.getDescripcion());
        dto.setContent(entity.getContenido());
        dto.setStatus(entity.getEstado());
        dto.setCreatedAt(entity.getCreadoEn());
        dto.setAuthor(entity.getGeneradoPor() == null ? "Sistema" : usuariosRepository.findById(entity.getGeneradoPor()).map(user -> user.getNombreUsuario() + " " + user.getApellidoUsuario()).orElse("Sistema"));
        return dto;
    }

    private boolean isEmployee(UsuariosEntity user) {
        return rolesRepository.findById(user.getIdRol()).map(role -> "EMPLEADO".equals(role.getNombreRol())).orElse(false);
    }

    private String defaultText(String value, String fallback) {
        String clean = TextUtils.cleanText(value);
        return clean.isEmpty() ? fallback : clean;
    }
}
