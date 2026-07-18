package SPITEC.Prestamo.Controller;

import SPITEC.Auth.Service.TokenService;
import SPITEC.Exception.AppException;
import SPITEC.Prestamo.DTO.PrestamoDTO;
import SPITEC.Prestamo.Service.PrestamoService;
import SPITEC.Usuarios.Entity.UsuariosEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loans")
@CrossOrigin
public class PrestamoController {
    private final PrestamoService service;
    private final TokenService tokenService;

    public PrestamoController(PrestamoService service, TokenService tokenService) {
        this.service = service;
        this.tokenService = tokenService;
    }

    @GetMapping
    public ResponseEntity<?> getLoans(@RequestHeader(value = "Authorization", required = false) String authorization) {
        UsuariosEntity actor = tokenService.requireUser(authorization);
        if (!service.canViewOperationalRecords(actor)) throw new AppException("Tu rol no puede ver ni gestionar prestamos.", 403);
        return ResponseEntity.ok(service.getLoans(actor));
    }

    @PostMapping
    public ResponseEntity<?> createLoan(@RequestHeader(value = "Authorization", required = false) String authorization, @RequestBody PrestamoDTO dto) {
        UsuariosEntity actor = tokenService.requireUser(authorization);
        if (!service.canViewOperationalRecords(actor)) throw new AppException("Tu rol no puede ver ni gestionar prestamos.", 403);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createLoanRequest(actor, dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateLoan(@RequestHeader(value = "Authorization", required = false) String authorization, @PathVariable Long id, @RequestBody PrestamoDTO dto) {
        UsuariosEntity actor = tokenService.requireUser(authorization);
        if (!service.canManageLoans(actor)) throw new AppException("Tu rol no puede aprobar o rechazar prestamos.", 403);
        return ResponseEntity.ok(service.updateLoanState(id, dto.getState()));
    }
}
