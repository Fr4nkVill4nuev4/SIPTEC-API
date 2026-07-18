package SPITEC.Roles.Service;

import SPITEC.Roles.DTO.RolesDTO;
import SPITEC.Roles.Entity.RolesEntity;
import SPITEC.Roles.Repository.RolesRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class RolesService {
    private final RolesRepository repo;

    public RolesService(RolesRepository repo) {
        this.repo = repo;
    }

    public RolesDTO nuevoRol(@Valid RolesDTO dto) {
        RolesEntity entity = convertirAEntity(dto);
        return convertirADTO(repo.save(entity));
    }

    private RolesEntity convertirAEntity(@Valid RolesDTO dto) {
        RolesEntity entity = new RolesEntity();
        entity.setNombreRol(dto.getNombreRol());
        return entity;
    }

    private RolesDTO convertirADTO(@Valid RolesEntity entity) {
        RolesDTO dto = new RolesDTO();
        dto.setId(entity.getId());
        dto.setNombreRol(entity.getNombreRol());
        return dto;
    }

    public List<RolesDTO> obtenerTodo() {
        return repo.findAll().stream().map(this::convertirADTO).toList();
    }

    public RolesDTO buscarPorId(Long id) {
        Optional<RolesEntity> entity = repo.findById(id);
        return entity.map(this::convertirADTO).orElse(null);
    }

    public boolean eliminarData(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }

    public RolesDTO actualizar(Long id, @Valid RolesDTO dto) {
        Optional<RolesEntity> optional = repo.findById(id);
        if (optional.isEmpty()) return null;
        RolesEntity entity = optional.get();
        entity.setNombreRol(dto.getNombreRol());
        return convertirADTO(repo.save(entity));
    }
}
