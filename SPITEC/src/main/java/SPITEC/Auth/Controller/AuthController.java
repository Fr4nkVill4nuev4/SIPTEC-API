package SPITEC.Auth.Controller;

import SPITEC.Auth.DTO.LoginDTO;
import SPITEC.Auth.Service.AuthService;
import SPITEC.Auth.Service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {
    private final AuthService service;
    private final TokenService tokenService;

    public AuthController(AuthService service, TokenService tokenService) {
        this.service = service;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO dto) {
        return ResponseEntity.ok(service.login(dto));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization", required = false) String authorization) {
        tokenService.logout(authorization);
        return ResponseEntity.ok(Map.of("ok", true));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestHeader(value = "Authorization", required = false) String authorization) {
        return ResponseEntity.ok(service.me(tokenService.requireUser(authorization)));
    }
}
