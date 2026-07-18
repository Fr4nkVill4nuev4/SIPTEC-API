package SPITEC.Historial.Controller;

import SPITEC.Auth.Service.TokenService;
import SPITEC.Exception.AppException;
import SPITEC.Historial.Service.HistorialService;
import SPITEC.Usuarios.Entity.UsuariosEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/history")
@CrossOrigin
public class HistorialController {
    private final HistorialService service;
    private final TokenService tokenService;

    public HistorialController(HistorialService service, TokenService tokenService) {
        this.service = service;
        this.tokenService = tokenService;
    }

    @GetMapping
    public ResponseEntity<?> getHistory(@RequestHeader(value = "Authorization", required = false) String authorization) {
        UsuariosEntity actor = tokenService.requireUser(authorization);
        if (!service.canViewOperationalRecords(actor)) throw new AppException("Tu rol no puede ver el historial.", 403);
        return ResponseEntity.ok(service.getHistory(actor));
    }
}
