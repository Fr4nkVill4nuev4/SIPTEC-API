package SPITEC.Reportes.Controller;

import SPITEC.Auth.Service.TokenService;
import SPITEC.Reportes.DTO.ReportesDTO;
import SPITEC.Reportes.Service.ReportesService;
import SPITEC.Usuarios.Entity.UsuariosEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin
public class ReportesController {
    private final ReportesService service;
    private final TokenService tokenService;

    public ReportesController(ReportesService service, TokenService tokenService) {
        this.service = service;
        this.tokenService = tokenService;
    }

    @GetMapping
    public ResponseEntity<?> getReports(@RequestHeader(value = "Authorization", required = false) String authorization) {
        UsuariosEntity actor = tokenService.requireUser(authorization);
        return ResponseEntity.ok(service.getReports(actor));
    }

    @PostMapping
    public ResponseEntity<?> createReport(@RequestHeader(value = "Authorization", required = false) String authorization, @RequestBody ReportesDTO dto) {
        UsuariosEntity actor = tokenService.requireUser(authorization);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createReport(dto, actor.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReport(@RequestHeader(value = "Authorization", required = false) String authorization, @PathVariable Long id) {
        UsuariosEntity actor = tokenService.requireUser(authorization);
        service.deleteReport(id, actor);
        return ResponseEntity.ok(Map.of("ok", true));
    }
}
