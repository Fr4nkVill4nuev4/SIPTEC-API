package SPITEC.Usuarios.Service;

import SPITEC.Exception.AppException;
import SPITEC.Instituciones.Entity.InstitucionesEntity;
import SPITEC.Instituciones.Repository.InstitucionesRepository;
import SPITEC.Roles.Entity.RolesEntity;
import SPITEC.Roles.Repository.RolesRepository;
import SPITEC.Usuarios.DTO.UsuariosDTO;
import SPITEC.Usuarios.Entity.UsuariosEntity;
import SPITEC.Usuarios.Repository.UsuariosRepository;
import SPITEC.Utils.TextUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsuariosService {
    private final UsuariosRepository repo;
    private final RolesRepository rolesRepository;
    private final InstitucionesRepository institucionesRepository;

    public UsuariosService(UsuariosRepository repo, RolesRepository rolesRepository, InstitucionesRepository institucionesRepository) {
        this.repo = repo;
        this.rolesRepository = rolesRepository;
        this.institucionesRepository = institucionesRepository;
    }

    public List<UsuariosDTO> getUsersFor(UsuariosEntity actor) {
        if (canManageUsers(actor)) return repo.findAll().stream().map(this::convertirADTO).toList();
        return List.of(convertirADTO(actor));
    }

    public UsuariosDTO createUser(UsuariosDTO dto) {
        String firstName = TextUtils.cleanText(dto.getFirstName());
        String lastName = TextUtils.cleanText(dto.getLastName());
        String email = TextUtils.cleanText(dto.getEmail()).toLowerCase();
        String password = TextUtils.cleanText(dto.getPassword());
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            throw new AppException("Nombre, apellido, correo y contrasena son obligatorios.", 400);
        }
        if (repo.findByCorreoUsuario(email).isPresent()) throw new AppException("Ya existe un usuario con ese correo.", 409);

        RolesEntity role = getRole(dto.getRole());
        InstitucionesEntity institution = getInstitution(dto.getInstitution());
        UsuariosEntity entity = new UsuariosEntity();
        entity.setNombreUsuario(firstName);
        entity.setApellidoUsuario(lastName);
        entity.setCorreoUsuario(email);
        entity.setPasswordHash(TextUtils.hashPassword(password));
        entity.setIdRol(role.getId());
        entity.setIdInstitucion(institution.getId());
        entity.setActivo(true);
        entity.setCreadoEn(LocalDateTime.now().toString());
        return convertirADTO(repo.save(entity));
    }

    public UsuariosDTO updateUser(Long id, UsuariosDTO dto) {
        UsuariosEntity entity = repo.findById(id).orElseThrow(() -> new AppException("Usuario no encontrado.", 404));
        String firstName = TextUtils.cleanText(dto.getFirstName());
        String lastName = TextUtils.cleanText(dto.getLastName());
        String email = TextUtils.cleanText(dto.getEmail()).toLowerCase();
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
            throw new AppException("Nombre, apellido y correo son obligatorios.", 400);
        }
        repo.findByCorreoUsuario(email).ifPresent(existing -> {
            if (!existing.getId().equals(id)) throw new AppException("Ya existe un usuario con ese correo.", 409);
        });
        entity.setNombreUsuario(firstName);
        entity.setApellidoUsuario(lastName);
        entity.setCorreoUsuario(email);
        if (!TextUtils.cleanText(dto.getPassword()).isEmpty()) entity.setPasswordHash(TextUtils.hashPassword(dto.getPassword()));
        entity.setIdRol(getRole(dto.getRole()).getId());
        entity.setIdInstitucion(getInstitution(dto.getInstitution()).getId());
        entity.setActivo(Boolean.TRUE.equals(dto.getActive()));
        return convertirADTO(repo.save(entity));
    }

    public UsuariosDTO toggleUser(Long id) {
        UsuariosEntity entity = repo.findById(id).orElseThrow(() -> new AppException("Usuario no encontrado", 404));
        entity.setActivo(!Boolean.TRUE.equals(entity.getActivo()));
        return convertirADTO(repo.save(entity));
    }

    public boolean canManageUsers(UsuariosEntity user) {
        return "ADMINISTRADOR".equals(getRoleName(user));
    }

    public String getRoleName(UsuariosEntity user) {
        return rolesRepository.findById(user.getIdRol()).map(RolesEntity::getNombreRol).orElse("");
    }

    public UsuariosDTO convertirADTO(UsuariosEntity entity) {
        UsuariosDTO dto = new UsuariosDTO();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getNombreUsuario());
        dto.setLastName(entity.getApellidoUsuario());
        dto.setEmail(entity.getCorreoUsuario());
        dto.setRole(rolesRepository.findById(entity.getIdRol()).map(RolesEntity::getNombreRol).orElse(""));
        dto.setInstitution(institucionesRepository.findById(entity.getIdInstitucion()).map(InstitucionesEntity::getNombreInstitucion).orElse(""));
        dto.setActive(entity.getActivo());
        return dto;
    }

    private RolesEntity getRole(String value) {
        String role = TextUtils.cleanText(value == null ? "EMPLEADO" : value).toUpperCase();
        return rolesRepository.findByNombreRol(role).orElseThrow(() -> new AppException("Rol o institucion no validos.", 400));
    }

    private InstitucionesEntity getInstitution(String value) {
        String institution = TextUtils.cleanText(value == null ? "ITR" : value).toUpperCase();
        return institucionesRepository.findByNombreInstitucion(institution).orElseThrow(() -> new AppException("Rol o institucion no validos.", 400));
    }
}
