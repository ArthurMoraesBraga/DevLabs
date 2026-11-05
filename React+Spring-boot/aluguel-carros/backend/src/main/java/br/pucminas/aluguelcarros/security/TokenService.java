package br.pucminas.aluguelcarros.security;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Emite e valida tokens de sessão (opacos) guardados em memória.
 * O front envia o token no header "Authorization: Bearer <token>".
 *
 * Evolução possível: trocar por JWT ou pelo Supabase Auth.
 */
@Service
public class TokenService {

    private final SecureRandom random = new SecureRandom();
    private final Map<String, Long> sessoes = new ConcurrentHashMap<>();

    public String gerarToken(Long usuarioId) {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        sessoes.put(token, usuarioId);
        return token;
    }

    public Optional<Long> validar(String token) {
        return Optional.ofNullable(sessoes.get(token));
    }

    public void revogar(String token) {
        sessoes.remove(token);
    }

    public void revogarTodosDoUsuario(Long usuarioId) {
        sessoes.values().removeIf(id -> id.equals(usuarioId));
    }
}
