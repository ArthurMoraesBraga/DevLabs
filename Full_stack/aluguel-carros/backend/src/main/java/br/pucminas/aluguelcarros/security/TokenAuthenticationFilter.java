package br.pucminas.aluguelcarros.security;

import br.pucminas.aluguelcarros.model.Usuario;
import br.pucminas.aluguelcarros.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lê o header Authorization, valida o token e coloca o {@link Usuario}
 * como "principal" no contexto do Spring Security.
 * Os controllers recebem o usuário com @AuthenticationPrincipal.
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private static final String PREFIXO = "Bearer ";

    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    public TokenAuthenticationFilter(TokenService tokenService, UsuarioRepository usuarioRepository) {
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith(PREFIXO)) {
            String token = header.substring(PREFIXO.length());
            tokenService.validar(token)
                    .flatMap(usuarioRepository::findById)
                    .ifPresent(this::autenticar);
        }
        chain.doFilter(request, response);
    }

    private void autenticar(Usuario usuario) {
        List<SimpleGrantedAuthority> papeis = new ArrayList<>();
        papeis.add(new SimpleGrantedAuthority("ROLE_" + usuario.getPerfil().name()));
        if (usuario.getPerfil().isAgente()) {
            papeis.add(new SimpleGrantedAuthority("ROLE_AGENTE"));
        }
        var auth = new UsernamePasswordAuthenticationToken(usuario, null, papeis);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
