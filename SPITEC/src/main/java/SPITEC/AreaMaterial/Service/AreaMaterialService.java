package SPITEC.AreaMaterial.Service;

import SPITEC.AreaMaterial.DTO.AreaMaterialDTO;
import SPITEC.AreaMaterial.Entity.AreaMaterialEntity;
import SPITEC.AreaMaterial.Repository.AreaMaterialRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AreaMaterialService {
    private final AreaMaterialRepository repo;

    public AreaMaterialService(AreaMaterialRepository repo) {
        this.repo = repo;
    }

    public AreaMaterialDTO nuevoAreaMaterial(@Valid AreaMaterialDTO dto) {
        try {
            AreaMaterialEntity entity = convertirAEntity(dto);
            AreaMaterialEntity entitySave = repo.save(entity);
            return convertirADTO(entitySave);
        } catch (Exception e) {
            log.error("Error al ingresar la informacion: {}", e.getMessage());
            return null;
        }
    }

    private AreaMaterialEntity convertirAEntity(@Valid AreaMaterialDTO dto) {
        AreaMaterialEntity objEntity = new AreaMaterialEntity();
        objEntity.setNombreArea(dto.getNombreArea());
        return objEntity;
    }

    private AreaMaterialDTO convertirADTO(@Valid AreaMaterialEntity entity) {
        AreaMaterialDTO objDTO = new AreaMaterialDTO();
        objDTO.setId(entity.getId());
        objDTO.setNombreArea(entity.getNombreArea());
        return objDTO;
    }

    public List<AreaMaterialDTO> obtenerTodo() {
        List<AreaMaterialEntity> data = repo.findAll();
        return data.stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    public AreaMaterialDTO buscarPorId(Long id) {
        Optional<AreaMaterialEntity> entidadOpcional = repo.findById(id);
        return entidadOpcional.map(this::convertirADTO).orElse(null);
    }

    public boolean eliminarData(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }

    public AreaMaterialDTO actualizar(Long id, @Valid AreaMaterialDTO dto) {
        try {
            Optional<AreaMaterialEntity> entidadOpcional = repo.findById(id);
            if (entidadOpcional.isPresent()) {
                AreaMaterialEntity entidad = entidadOpcional.get();
                entidad.setNombreArea(dto.getNombreArea());
                AreaMaterialEntity datosGuardados = repo.save(entidad);
                return convertirADTO(datosGuardados);
            }
        } catch (Exception e) {
            log.error("Oops, ocurrio un error al procesar la informacion: {}", e.getMessage());
            return null;
        }
        return null;
    }
}
