package SPITEC.DetallePrestamo.Service;

import SPITEC.DetallePrestamo.DTO.DetalleDTO;
import SPITEC.DetallePrestamo.Entity.DetalleEntity;
import SPITEC.DetallePrestamo.Repository.DetalleRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class DetalleService {
    private final DetalleRepository repo;

    public DetalleService(DetalleRepository repo) {
        this.repo = repo;
    }

    public DetalleDTO nuevoDetalle(@Valid DetalleDTO dto) {
        DetalleEntity entity = convertirAEntity(dto);
        return convertirADTO(repo.save(entity));
    }

    private DetalleEntity convertirAEntity(@Valid DetalleDTO dto) {
        DetalleEntity entity = new DetalleEntity();
        entity.setIdPrestamo(dto.getIdPrestamo());
        return entity;
    }

    private DetalleDTO convertirADTO(@Valid DetalleEntity entity) {
        DetalleDTO dto = new DetalleDTO();
        dto.setId(entity.getId());
        dto.setIdPrestamo(entity.getIdPrestamo());
        return dto;
    }

    public List<DetalleDTO> obtenerTodo() {
        return repo.findAll().stream().map(this::convertirADTO).toList();
    }

    public DetalleDTO buscarPorId(Long id) {
        Optional<DetalleEntity> entity = repo.findById(id);
        return entity.map(this::convertirADTO).orElse(null);
    }

    public boolean eliminarData(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }

    public DetalleDTO actualizar(Long id, @Valid DetalleDTO dto) {
        Optional<DetalleEntity> optional = repo.findById(id);
        if (optional.isEmpty()) return null;
        DetalleEntity entity = optional.get();
        entity.setIdPrestamo(dto.getIdPrestamo());
        return convertirADTO(repo.save(entity));
    }
}
