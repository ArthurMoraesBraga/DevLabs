package br.pucminas.aluguelcarros.service;

import br.pucminas.aluguelcarros.dto.ClienteRequest;
import br.pucminas.aluguelcarros.dto.RendimentoDTO;
import br.pucminas.aluguelcarros.exception.AcessoNegadoException;
import br.pucminas.aluguelcarros.exception.RecursoNaoEncontradoException;
import br.pucminas.aluguelcarros.exception.RegraNegocioException;
import br.pucminas.aluguelcarros.model.Cliente;
import br.pucminas.aluguelcarros.model.PedidoAluguel;
import br.pucminas.aluguelcarros.model.Usuario;
import br.pucminas.aluguelcarros.repository.PedidoAluguelRepository;
import br.pucminas.aluguelcarros.repository.UsuarioRepository;
import br.pucminas.aluguelcarros.security.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/** CRUD de cliente. */
@Service
public class ClienteService {

    private final UsuarioRepository usuarioRepository;
    private final PedidoAluguelRepository pedidoRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public ClienteService(UsuarioRepository usuarioRepository, PedidoAluguelRepository pedidoRepository,
                          PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.usuarioRepository = usuarioRepository;
        this.pedidoRepository = pedidoRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    public Cliente criar(ClienteRequest req) {
        if (req.senha() == null || req.senha().length() < 6) {
            throw new RegraNegocioException("A senha deve ter ao menos 6 caracteres.");
        }
        validarUnicidade(req, null);

        Cliente cliente = new Cliente();
        preencher(cliente, req);
        cliente.setSenha(passwordEncoder.encode(req.senha()));
        return (Cliente) usuarioRepository.save(cliente);
    }

    public List<Cliente> listar() {
        return usuarioRepository.findAllClientes();
    }

    public Cliente buscar(Long id, Usuario logado) {
        verificarAcesso(id, logado);
        return buscarPorId(id);
    }

    public Cliente atualizar(Long id, ClienteRequest req, Usuario logado) {
        verificarAcesso(id, logado);
        Cliente cliente = buscarPorId(id);
        validarUnicidade(req, id);

        preencher(cliente, req);
        if (req.senha() != null && !req.senha().isBlank()) {
            if (req.senha().length() < 6) {
                throw new RegraNegocioException("A senha deve ter ao menos 6 caracteres.");
            }
            cliente.setSenha(passwordEncoder.encode(req.senha()));
        }
        return (Cliente) usuarioRepository.save(cliente);
    }

    public void excluir(Long id, Usuario logado) {
        verificarAcesso(id, logado);
        buscarPorId(id);

        List<PedidoAluguel> pedidos = pedidoRepository.findByClienteId(id);
        if (pedidos.stream().anyMatch(p -> p.getStatus().isAtivo())) {
            throw new RegraNegocioException("Cliente possui pedidos ativos. Cancele-os antes de excluir.");
        }
        pedidos.forEach(p -> pedidoRepository.deleteById(p.getId()));
        tokenService.revogarTodosDoUsuario(id);
        usuarioRepository.deleteById(id);
    }

    Cliente buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .filter(Cliente.class::isInstance)
                .map(Cliente.class::cast)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente " + id + " não encontrado."));
    }

    /** Cliente só acessa os próprios dados; agentes acessam qualquer cliente. */
    private void verificarAcesso(Long clienteId, Usuario logado) {
        if (!logado.getPerfil().isAgente() && !logado.getId().equals(clienteId)) {
            throw new AcessoNegadoException("cliente só pode acessar o próprio cadastro.");
        }
    }

    private void validarUnicidade(ClienteRequest req, Long idAtual) {
        usuarioRepository.findByLogin(req.login())
                .filter(u -> !u.getId().equals(idAtual))
                .ifPresent(u -> { throw new RegraNegocioException("Usuário (login) já está em uso."); });
        usuarioRepository.findClienteByCpf(req.cpf())
                .filter(c -> !c.getId().equals(idAtual))
                .ifPresent(c -> { throw new RegraNegocioException("CPF já cadastrado."); });
    }

    private void preencher(Cliente c, ClienteRequest req) {
        c.setLogin(req.login().trim());
        c.setNome(req.nome().trim());
        c.setEndereco(req.endereco().trim());
        c.setRg(req.rg().trim());
        c.setCpf(req.cpf());
        c.setProfissao(req.profissao().trim());
        c.setRendimentos(req.rendimentos() == null ? List.of()
                : req.rendimentos().stream().map(RendimentoDTO::toModel).toList());
    }
}
