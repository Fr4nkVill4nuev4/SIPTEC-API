package SPITEC.Returns.Controller;

import SPITEC.Auth.Service.TokenService;
import SPITEC.Exception.AppException;
import SPITEC.Returns.DTO.ReturnsDTO;
import SPITEC.Returns.Service.ReturnsService;
import SPITEC.Usuarios.Entity.UsuariosEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/returns")
@CrossOrigin
public class ReturnsController {
    private final ReturnsService service;
    private final TokenService tokenService;

    public ReturnsController(ReturnsService service, TokenService tokenService) {
        this.service = service;
        this.tokenService = tokenService;
    }

    @GetMapping
    public ResponseEntity<?> getReturns(@RequestHeader(value = "Authorization", required = false) String authorization) {
        UsuariosEntity actor = tokenService.requireUser(authorization);
        if (!service.canViewOperationalRecords(actor)) throw new AppException("Tu rol no puede ver ni gestionar devoluciones.", 403);
        return ResponseEntity.ok(service.getReturns(actor));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateReturn(@RequestHeader(value = "Authorization", required = false) String authorization, @PathVariable Long id, @RequestBody ReturnsDTO dto) {
        UsuariosEntity actor = tokenService.requireUser(authorization);
        return ResponseEntity.ok(service.updateReturn(id, dto, actor));
    }
}
