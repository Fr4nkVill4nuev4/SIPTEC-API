package SPITEC.Historial.Service;

import SPITEC.Historial.DTO.HistorialDTO;
import SPITEC.Historial.Entity.HistorialEntity;
import SPITEC.Historial.Repository.HistorialRepository;
import SPITEC.Roles.Repository.RolesRepository;
import SPITEC.Usuarios.Entity.UsuariosEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistorialService {
    private final HistorialRepository repo;
    private final RolesRepository rolesRepository;

    public HistorialService(HistorialRepository repo, RolesRepository rolesRepository) {
        this.repo = repo;
        this.rolesRepository = rolesRepository;
    }

    public List<HistorialDTO> getHistory(UsuariosEntity actor) {
        List<HistorialEntity> data = isEmployee(actor) ? repo.findByIdUsuarioOrderByIdDesc(actor.getId()) : repo.findAllByOrderByIdDesc();
        return data.stream().map(this::toDTO).toList();
    }

    public boolean canViewOperationalRecords(UsuariosEntity user) {
        return !"IT".equals(getRole(user));
    }

    private HistorialDTO toDTO(HistorialEntity entity) {
        HistorialDTO dto = new HistorialDTO();
        dto.setCode(entity.getCodigoInventario());
        dto.setItem(entity.getNombreMaterial());
        dto.setStart(entity.getFechaInicio());
        dto.setEnd(entity.getFechaFin());
        dto.setUser(entity.getNombreUsuario());
        dto.setStatus(entity.getEstado());
        return dto;
    }

    private boolean isEmployee(UsuariosEntity user) { return "EMPLEADO".equals(getRole(user)); }
    private String getRole(UsuariosEntity user) { return rolesRepository.findById(user.getIdRol()).map(role -> role.getNombreRol()).orElse(""); }
}
