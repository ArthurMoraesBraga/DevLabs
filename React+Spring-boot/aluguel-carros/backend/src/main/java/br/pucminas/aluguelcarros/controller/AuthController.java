package br.pucminas.aluguelcarros.controller;

import br.pucminas.aluguelcarros.dto.LoginRequest;
import br.pucminas.aluguelcarros.dto.LoginResponse;
import br.pucminas.aluguelcarros.dto.UsuarioResponse;
import br.pucminas.aluguelcarros.model.Usuario;
import br.pucminas.aluguelcarros.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginRequest req) {
        return authService.login(req);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String header) {
        authService.logout(header.replaceFirst("^Bearer ", ""));
        return ResponseEntity.noContent().build();
    }

    /** Retorna o usuário dono do token (o front usa para restaurar a sessão). */
    @GetMapping("/me")
    public UsuarioResponse me(@AuthenticationPrincipal Usuario usuario) {
        return UsuarioResponse.from(usuario);
    }
}
