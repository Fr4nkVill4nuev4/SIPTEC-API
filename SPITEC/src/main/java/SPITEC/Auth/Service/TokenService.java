package SPITEC.Auth.Service;

import SPITEC.Auth.Entity.AuthSessionEntity;
import SPITEC.Auth.Repository.AuthSessionRepository;
import SPITEC.Exception.AppException;
import SPITEC.Usuarios.Entity.UsuariosEntity;
import SPITEC.Usuarios.Repository.UsuariosRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.HexFormat;

@Service
public class TokenService {
    private static final long SESSION_TTL_MS = 1000L * 60 * 60 * 8;
    private final AuthSessionRepository repo;
    private final UsuariosRepository usuariosRepository;
    private final SecureRandom random = new SecureRandom();

    public TokenService(AuthSessionRepository repo, UsuariosRepository usuariosRepository) {
        this.repo = repo;
        this.usuariosRepository = usuariosRepository;
    }

    public String createToken(Long userId) {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        AuthSessionEntity session = new AuthSessionEntity();
        session.setToken(HexFormat.of().formatHex(bytes));
        session.setUserId(userId);
        session.setExpiresAt(System.currentTimeMillis() + SESSION_TTL_MS);
        return repo.save(session).getToken();
    }

    public AuthSessionEntity requireSession(String header) {
        String token = getToken(header);
        if (token == null) throw new AppException("Sesion no valida. Inicie sesion nuevamente.", 401);
        AuthSessionEntity session = repo.findByToken(token).orElseThrow(() -> new AppException("Sesion expirada. Inicie sesion nuevamente.", 401));
        if (session.getExpiresAt() < System.currentTimeMillis()) {
            repo.delete(session);
            throw new AppException("Sesion expirada. Inicie sesion nuevamente.", 401);
        }
        session.setExpiresAt(System.currentTimeMillis() + SESSION_TTL_MS);
        return repo.save(session);
    }

    public UsuariosEntity requireUser(String header) {
        AuthSessionEntity session = requireSession(header);
        return usuariosRepository.findById(session.getUserId()).orElseThrow(() -> new AppException("Usuario no encontrado.", 404));
    }

    public void logout(String header) {
        String token = getToken(header);
        if (token != null) repo.deleteByToken(token);
    }

    private String getToken(String header) {
        if (header != null && header.startsWith("Bearer ")) return header.substring(7);
        return null;
    }
}
