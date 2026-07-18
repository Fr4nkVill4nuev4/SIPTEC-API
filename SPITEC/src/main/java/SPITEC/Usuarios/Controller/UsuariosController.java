package SPITEC.Usuarios.Controller;

import SPITEC.Auth.Service.TokenService;
import SPITEC.Exception.AppException;
import SPITEC.Usuarios.DTO.UsuariosDTO;
import SPITEC.Usuarios.Entity.UsuariosEntity;
import SPITEC.Usuarios.Service.UsuariosService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UsuariosController {
    private final UsuariosService service;
    private final TokenService tokenService;

    public UsuariosController(UsuariosService service, TokenService tokenService) {
        this.service = service;
        this.tokenService = tokenService;
    }

    @GetMapping
    public ResponseEntity<?> getUsers(@RequestHeader(value = "Authorization", required = false) String authorization) {
        UsuariosEntity actor = tokenService.requireUser(authorization);
        return ResponseEntity.ok(service.getUsersFor(actor));
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestHeader(value = "Authorization", required = false) String authorization, @RequestBody UsuariosDTO dto) {
        UsuariosEntity actor = tokenService.requireUser(authorization);
        if (!service.canManageUsers(actor)) throw new AppException("Solo el administrador puede crear usuarios.", 403);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createUser(dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateUser(@RequestHeader(value = "Authorization", required = false) String authorization, @PathVariable Long id, @RequestBody UsuariosDTO dto) {
        UsuariosEntity actor = tokenService.requireUser(authorization);
        if (!service.canManageUsers(actor)) throw new AppException("Solo el administrador puede editar usuarios.", 403);
        return ResponseEntity.ok(service.updateUser(id, dto));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<?> toggleUser(@RequestHeader(value = "Authorization", required = false) String authorization, @PathVariable Long id) {
        UsuariosEntity actor = tokenService.requireUser(authorization);
        if (!service.canManageUsers(actor)) throw new AppException("Solo el administrador puede cambiar usuarios.", 403);
        return ResponseEntity.ok(service.toggleUser(id));
    }
}
