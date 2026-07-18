package SPITEC.Auth.Service;

import SPITEC.Auth.DTO.AuthResponseDTO;
import SPITEC.Auth.DTO.LoginDTO;
import SPITEC.Auth.DTO.LoginUserDTO;
import SPITEC.Exception.AppException;
import SPITEC.Instituciones.Repository.InstitucionesRepository;
import SPITEC.Roles.Repository.RolesRepository;
import SPITEC.Usuarios.Entity.UsuariosEntity;
import SPITEC.Usuarios.Repository.UsuariosRepository;
import SPITEC.Utils.TextUtils;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UsuariosRepository usuariosRepository;
    private final RolesRepository rolesRepository;
    private final InstitucionesRepository institucionesRepository;
    private final TokenService tokenService;

    public AuthService(UsuariosRepository usuariosRepository, RolesRepository rolesRepository, InstitucionesRepository institucionesRepository, TokenService tokenService) {
        this.usuariosRepository = usuariosRepository;
        this.rolesRepository = rolesRepository;
        this.institucionesRepository = institucionesRepository;
        this.tokenService = tokenService;
    }

    public AuthResponseDTO login(LoginDTO dto) {
        String email = TextUtils.cleanText(dto.getEmail()).toLowerCase();
        UsuariosEntity user = usuariosRepository.findByCorreoUsuario(email).orElseThrow(() -> new AppException("Credenciales incorrectas.", 401));
        if (!user.getPasswordHash().equals(TextUtils.hashPassword(dto.getPassword()))) throw new AppException("Credenciales incorrectas.", 401);
        if (!Boolean.TRUE.equals(user.getActivo())) throw new AppException("Usuario inactivo. Contacte al administrador.", 403);
        return new AuthResponseDTO(tokenService.createToken(user.getId()), toLoginUser(user));
    }

    public LoginUserDTO me(UsuariosEntity user) {
        return toLoginUser(user);
    }

    private LoginUserDTO toLoginUser(UsuariosEntity user) {
        String role = rolesRepository.findById(user.getIdRol()).map(item -> item.getNombreRol()).orElse("");
        String institution = institucionesRepository.findById(user.getIdInstitucion()).map(item -> item.getNombreInstitucion()).orElse("");
        return new LoginUserDTO(user.getId(), user.getNombreUsuario(), user.getApellidoUsuario(), user.getCorreoUsuario(), role, institution, user.getActivo());
    }
}
