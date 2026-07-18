package SPITEC.Permiso.Service;

import SPITEC.Permiso.DTO.PermisosDTO;
import SPITEC.Permiso.Entity.PermisosEntity;
import SPITEC.Permiso.Repository.PermisosRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PermisosService {
    private final PermisosRepository repo;

    public PermisosService(PermisosRepository repo) {
        this.repo = repo;
    }

    public PermisosDTO nuevoPermiso(@Valid PermisosDTO dto) {
        PermisosEntity entity = convertirAEntity(dto);
        return convertirADTO(repo.save(entity));
    }

    private PermisosEntity convertirAEntity(@Valid PermisosDTO dto) {
        PermisosEntity entity = new PermisosEntity();
        entity.setNombrePermiso(dto.getNombrePermiso());
        return entity;
    }

    private PermisosDTO convertirADTO(@Valid PermisosEntity entity) {
        PermisosDTO dto = new PermisosDTO();
        dto.setId(entity.getId());
        dto.setNombrePermiso(entity.getNombrePermiso());
        return dto;
    }

    public List<PermisosDTO> obtenerTodo() {
        return repo.findAll().stream().map(this::convertirADTO).toList();
    }

    public PermisosDTO buscarPorId(Long id) {
        Optional<PermisosEntity> entity = repo.findById(id);
        return entity.map(this::convertirADTO).orElse(null);
    }

    public boolean eliminarData(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }

    public PermisosDTO actualizar(Long id, @Valid PermisosDTO dto) {
        Optional<PermisosEntity> optional = repo.findById(id);
        if (optional.isEmpty()) return null;
        PermisosEntity entity = optional.get();
        entity.setNombrePermiso(dto.getNombrePermiso());
        return convertirADTO(repo.save(entity));
    }
}
