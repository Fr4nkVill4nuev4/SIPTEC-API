package SPITEC.Inventario.Controller;

import SPITEC.Auth.Service.TokenService;
import SPITEC.Exception.AppException;
import SPITEC.Inventario.DTO.InventarioDTO;
import SPITEC.Inventario.Service.InventarioService;
import SPITEC.Usuarios.Entity.UsuariosEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin
public class InventarioController {
    private final InventarioService service;
    private final TokenService tokenService;

    public InventarioController(InventarioService service, TokenService tokenService) {
        this.service = service;
        this.tokenService = tokenService;
    }

    @GetMapping
    public ResponseEntity<?> getInventory(@RequestHeader(value = "Authorization", required = false) String authorization) {
        UsuariosEntity actor = tokenService.requireUser(authorization);
        return ResponseEntity.ok(service.getInventory(actor));
    }

    @PostMapping
    public ResponseEntity<?> createInventory(@RequestHeader(value = "Authorization", required = false) String authorization, @RequestBody InventarioDTO dto) {
        UsuariosEntity actor = tokenService.requireUser(authorization);
        if (!service.canManageInventory(actor)) throw new AppException("Tu rol no puede agregar inventario.", 403);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createInventoryItem(dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateInventory(@RequestHeader(value = "Authorization", required = false) String authorization, @PathVariable Long id, @RequestBody InventarioDTO dto) {
        UsuariosEntity actor = tokenService.requireUser(authorization);
        if (!service.canManageInventory(actor)) throw new AppException("Tu rol no puede editar inventario.", 403);
        return ResponseEntity.ok(service.updateInventoryItem(id, dto));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> cycleStatus(@RequestHeader(value = "Authorization", required = false) String authorization, @PathVariable Long id) {
        UsuariosEntity actor = tokenService.requireUser(authorization);
        if (!service.canManageInventory(actor)) throw new AppException("Tu rol no puede cambiar inventario.", 403);
        return ResponseEntity.ok(service.cycleInventoryStatus(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInventory(@RequestHeader(value = "Authorization", required = false) String authorization, @PathVariable Long id) {
        UsuariosEntity actor = tokenService.requireUser(authorization);
        if (!service.canManageInventory(actor)) throw new AppException("Tu rol no puede eliminar inventario.", 403);
        service.deleteInventoryItem(id);
        return ResponseEntity.ok(Map.of("ok", true));
    }
}
