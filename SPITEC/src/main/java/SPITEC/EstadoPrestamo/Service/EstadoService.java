package SPITEC.EstadoPrestamo.Service;

import SPITEC.EstadoPrestamo.DTO.EstadoDTO;
import SPITEC.EstadoPrestamo.Entity.EstadoEntity;
import SPITEC.EstadoPrestamo.Repository.EstadoRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EstadoService {
    private final EstadoRepository repo;

    public EstadoService(EstadoRepository repo) {
        this.repo = repo;
    }

    public EstadoDTO nuevoEstado(@Valid EstadoDTO dto) {
        try {
            EstadoEntity entity = convertirAEntity(dto);
            EstadoEntity entitySave = repo.save(entity);
            return convertirADTO(entitySave);
        } catch (Exception e) {
            log.error("Error al ingresar la informacion: {}", e.getMessage());
            return null;
        }
    }

    private EstadoEntity convertirAEntity(@Valid EstadoDTO dto) {
        EstadoEntity objEntity = new EstadoEntity();
        objEntity.setNombreEstado(dto.getNombreEstado());
        return objEntity;
    }

    private EstadoDTO convertirADTO(@Valid EstadoEntity entity) {
        EstadoDTO objDTO = new EstadoDTO();
        objDTO.setId(entity.getId());
        objDTO.setNombreEstado(entity.getNombreEstado());
        return objDTO;
    }

    public List<EstadoDTO> obtenerTodo() {
        List<EstadoEntity> data = repo.findAll();
        return data.stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    public EstadoDTO buscarPorId(Long id) {
        Optional<EstadoEntity> entidadOpcional = repo.findById(id);
        return entidadOpcional.map(this::convertirADTO).orElse(null);
    }

    public boolean eliminarData(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }

    public EstadoDTO actualizar(Long id, @Valid EstadoDTO dto) {
        try {
            Optional<EstadoEntity> entidadOpcional = repo.findById(id);
            if (entidadOpcional.isPresent()) {
                EstadoEntity entidad = entidadOpcional.get();
                entidad.setNombreEstado(dto.getNombreEstado());
                EstadoEntity datosGuardados = repo.save(entidad);
                return convertirADTO(datosGuardados);
            }
        } catch (Exception e) {
            log.error("Oops, ocurrio un error al procesar la informacion: {}", e.getMessage());
            return null;
        }
        return null;
    }
}
