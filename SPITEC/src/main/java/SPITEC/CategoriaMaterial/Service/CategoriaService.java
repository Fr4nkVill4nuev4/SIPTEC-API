package SPITEC.CategoriaMaterial.Service;

import SPITEC.CategoriaMaterial.DTO.CategoriaDTO;
import SPITEC.CategoriaMaterial.Entity.CategoriaEntity;
import SPITEC.CategoriaMaterial.Repository.CategoriaRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CategoriaService {
    private final CategoriaRepository repo;

    public CategoriaService(CategoriaRepository repo) {
        this.repo = repo;
    }

    public CategoriaDTO nuevoCategoria(@Valid CategoriaDTO dto) {
        CategoriaEntity entity = convertirAEntity(dto);
        return convertirADTO(repo.save(entity));
    }

    private CategoriaEntity convertirAEntity(@Valid CategoriaDTO dto) {
        CategoriaEntity entity = new CategoriaEntity();
        entity.setNombreCategoria(dto.getNombreCategoria());
        return entity;
    }

    private CategoriaDTO convertirADTO(@Valid CategoriaEntity entity) {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setId(entity.getId());
        dto.setNombreCategoria(entity.getNombreCategoria());
        return dto;
    }

    public List<CategoriaDTO> obtenerTodo() {
        return repo.findAll().stream().map(this::convertirADTO).toList();
    }

    public CategoriaDTO buscarPorId(Long id) {
        Optional<CategoriaEntity> entity = repo.findById(id);
        return entity.map(this::convertirADTO).orElse(null);
    }

    public boolean eliminarData(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }

    public CategoriaDTO actualizar(Long id, @Valid CategoriaDTO dto) {
        Optional<CategoriaEntity> optional = repo.findById(id);
        if (optional.isEmpty()) return null;
        CategoriaEntity entity = optional.get();
        entity.setNombreCategoria(dto.getNombreCategoria());
        return convertirADTO(repo.save(entity));
    }
}
