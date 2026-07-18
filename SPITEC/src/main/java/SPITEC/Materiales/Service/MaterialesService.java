package SPITEC.Materiales.Service;

import SPITEC.Materiales.DTO.MaterialesDTO;
import SPITEC.Materiales.Entity.MaterialesEntity;
import SPITEC.Materiales.Repository.MaterialesRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class MaterialesService {
    private final MaterialesRepository repo;

    public MaterialesService(MaterialesRepository repo) {
        this.repo = repo;
    }

    public MaterialesDTO nuevoMaterial(@Valid MaterialesDTO dto) {
        MaterialesEntity entity = convertirAEntity(dto);
        return convertirADTO(repo.save(entity));
    }

    private MaterialesEntity convertirAEntity(@Valid MaterialesDTO dto) {
        MaterialesEntity entity = new MaterialesEntity();
        entity.setNombreMaterial(dto.getNombreMaterial());
        return entity;
    }

    private MaterialesDTO convertirADTO(@Valid MaterialesEntity entity) {
        MaterialesDTO dto = new MaterialesDTO();
        dto.setId(entity.getId());
        dto.setNombreMaterial(entity.getNombreMaterial());
        return dto;
    }

    public List<MaterialesDTO> obtenerTodo() {
        return repo.findAll().stream().map(this::convertirADTO).toList();
    }

    public MaterialesDTO buscarPorId(Long id) {
        Optional<MaterialesEntity> entity = repo.findById(id);
        return entity.map(this::convertirADTO).orElse(null);
    }

    public boolean eliminarData(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }

    public MaterialesDTO actualizar(Long id, @Valid MaterialesDTO dto) {
        Optional<MaterialesEntity> optional = repo.findById(id);
        if (optional.isEmpty()) return null;
        MaterialesEntity entity = optional.get();
        entity.setNombreMaterial(dto.getNombreMaterial());
        return convertirADTO(repo.save(entity));
    }
}
