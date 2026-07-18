package SPITEC.Instituciones.Service;

import SPITEC.Instituciones.DTO.InstitucionesDTO;
import SPITEC.Instituciones.Entity.InstitucionesEntity;
import SPITEC.Instituciones.Repository.InstitucionesRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InstitucionesService {
    private final InstitucionesRepository repo;

    public InstitucionesService(InstitucionesRepository repo) {
        this.repo = repo;
    }

    public InstitucionesDTO nuevoInstitucion(@Valid InstitucionesDTO dto) {
        try {
            InstitucionesEntity entity = convertirAEntity(dto);
            InstitucionesEntity entitySave = repo.save(entity);
            return convertirADTO(entitySave);
        } catch (Exception e) {
            log.error("Error al ingresar la informacion: {}", e.getMessage());
            return null;
        }
    }

    private InstitucionesEntity convertirAEntity(@Valid InstitucionesDTO dto) {
        InstitucionesEntity objEntity = new InstitucionesEntity();
        objEntity.setNombreInstitucion(dto.getNombreInstitucion());
        return objEntity;
    }

    private InstitucionesDTO convertirADTO(@Valid InstitucionesEntity entity) {
        InstitucionesDTO objDTO = new InstitucionesDTO();
        objDTO.setId(entity.getId());
        objDTO.setNombreInstitucion(entity.getNombreInstitucion());
        return objDTO;
    }

    public List<InstitucionesDTO> obtenerTodo() {
        List<InstitucionesEntity> data = repo.findAll();
        return data.stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    public InstitucionesDTO buscarPorId(Long id) {
        Optional<InstitucionesEntity> entidadOpcional = repo.findById(id);
        return entidadOpcional.map(this::convertirADTO).orElse(null);
    }

    public boolean eliminarData(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }

    public InstitucionesDTO actualizar(Long id, @Valid InstitucionesDTO dto) {
        try {
            Optional<InstitucionesEntity> entidadOpcional = repo.findById(id);
            if (entidadOpcional.isPresent()) {
                InstitucionesEntity entidad = entidadOpcional.get();
                entidad.setNombreInstitucion(dto.getNombreInstitucion());
                InstitucionesEntity datosGuardados = repo.save(entidad);
                return convertirADTO(datosGuardados);
            }
        } catch (Exception e) {
            log.error("Oops, ocurrio un error al procesar la informacion: {}", e.getMessage());
            return null;
        }
        return null;
    }
}
