package br.pucminas.aluguelcarros.service;

import br.pucminas.aluguelcarros.dto.AgenteRequest;
import br.pucminas.aluguelcarros.exception.RecursoNaoEncontradoException;
import br.pucminas.aluguelcarros.exception.RegraNegocioException;
import br.pucminas.aluguelcarros.model.Agente;
import br.pucminas.aluguelcarros.model.Banco;
import br.pucminas.aluguelcarros.model.Empresa;
import br.pucminas.aluguelcarros.model.Perfil;
import br.pucminas.aluguelcarros.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgenteService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AgenteService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Agente criar(AgenteRequest req) {
        if (!req.tipo().isAgente()) {
            throw new RegraNegocioException("Tipo de agente deve ser EMPRESA ou BANCO.");
        }
        if (usuarioRepository.existsByLogin(req.login())) {
            throw new RegraNegocioException("Usuário (login) já está em uso.");
        }
        Agente agente = req.tipo() == Perfil.BANCO ? new Banco() : new Empresa();
        agente.setLogin(req.login().trim());
        agente.setSenha(passwordEncoder.encode(req.senha()));
        agente.setNome(req.nome().trim());
        agente.setEndereco(req.endereco().trim());
        agente.setCnpj(req.cnpj());
        return (Agente) usuarioRepository.save(agente);
    }

    public List<Agente> listar(Perfil tipo) {
        return usuarioRepository.findAllAgentes().stream()
                .filter(a -> tipo == null || a.getPerfil() == tipo)
                .toList();
    }

    public Banco buscarBanco(Long id) {
        return usuarioRepository.findById(id)
                .filter(Banco.class::isInstance)
                .map(Banco.class::cast)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Banco " + id + " não encontrado."));
    }
}
