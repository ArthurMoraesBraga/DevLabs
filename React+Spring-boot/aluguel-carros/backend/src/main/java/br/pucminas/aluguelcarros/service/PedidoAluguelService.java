package br.pucminas.aluguelcarros.service;

import br.pucminas.aluguelcarros.dto.AvaliacaoRequest;
import br.pucminas.aluguelcarros.dto.PedidoRequest;
import br.pucminas.aluguelcarros.exception.AcessoNegadoException;
import br.pucminas.aluguelcarros.exception.RecursoNaoEncontradoException;
import br.pucminas.aluguelcarros.exception.RegraNegocioException;
import br.pucminas.aluguelcarros.model.Agente;
import br.pucminas.aluguelcarros.model.Automovel;
import br.pucminas.aluguelcarros.model.Cliente;
import br.pucminas.aluguelcarros.model.PedidoAluguel;
import br.pucminas.aluguelcarros.model.Usuario;
import br.pucminas.aluguelcarros.repository.PedidoAluguelRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/** Gestão de pedidos: introduzir, consultar, modificar, cancelar e avaliar. */
@Service
public class PedidoAluguelService {

    private final PedidoAluguelRepository pedidoRepository;
    private final AutomovelService automovelService;
    private final ClienteService clienteService;

    public PedidoAluguelService(PedidoAluguelRepository pedidoRepository, AutomovelService automovelService,
                                ClienteService clienteService) {
        this.pedidoRepository = pedidoRepository;
        this.automovelService = automovelService;
        this.clienteService = clienteService;
    }

    public PedidoAluguel criar(PedidoRequest req, Usuario logado) {
        Cliente cliente = clienteService.buscarPorId(logado.getId());
        Automovel automovel = automovelService.buscar(req.automovelId());

        PedidoAluguel pedido = new PedidoAluguel();
        pedido.setCliente(cliente);
        pedido.setAutomovel(automovel);
        pedido.definirPeriodo(req.dataInicio(), req.dataFim());
        verificarDisponibilidade(automovel, req.dataInicio(), req.dataFim(), null);
        return pedidoRepository.save(pedido);
    }

    /** Cliente vê apenas os próprios pedidos; agentes veem todos. */
    public List<PedidoAluguel> listar(Usuario logado) {
        List<PedidoAluguel> pedidos = logado.getPerfil().isAgente()
                ? pedidoRepository.findAll()
                : pedidoRepository.findByClienteId(logado.getId());
        return pedidos.stream()
                .sorted((a, b) -> b.getDataCriacao().compareTo(a.getDataCriacao()))
                .toList();
    }

    public PedidoAluguel buscar(Long id, Usuario logado) {
        PedidoAluguel pedido = buscarPorId(id);
        verificarAcesso(pedido, logado);
        return pedido;
    }

    public PedidoAluguel atualizar(Long id, PedidoRequest req, Usuario logado) {
        PedidoAluguel pedido = buscar(id, logado);
        if (logado.getPerfil().isAgente()) {
            pedido.validarModificacaoPeloAgente();
        } else {
            pedido.validarModificacaoPeloCliente();
        }
        Automovel automovel = automovelService.buscar(req.automovelId());
        verificarDisponibilidade(automovel, req.dataInicio(), req.dataFim(), pedido.getId());

        pedido.setAutomovel(automovel);
        pedido.definirPeriodo(req.dataInicio(), req.dataFim());
        return pedidoRepository.save(pedido);
    }

    public PedidoAluguel cancelar(Long id, Usuario logado) {
        PedidoAluguel pedido = buscar(id, logado);
        pedido.cancelar();
        return pedidoRepository.save(pedido);
    }

    public PedidoAluguel avaliar(Long id, AvaliacaoRequest req, Usuario logado) {
        PedidoAluguel pedido = buscarPorId(id);
        pedido.avaliar((Agente) logado, req.aprovado(), req.parecer().trim());
        return pedidoRepository.save(pedido);
    }

    PedidoAluguel buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido " + id + " não encontrado."));
    }

    PedidoAluguel salvar(PedidoAluguel pedido) {
        return pedidoRepository.save(pedido);
    }

    private void verificarAcesso(PedidoAluguel pedido, Usuario logado) {
        if (!logado.getPerfil().isAgente() && !pedido.getCliente().getId().equals(logado.getId())) {
            throw new AcessoNegadoException("o pedido pertence a outro cliente.");
        }
    }

    private void verificarDisponibilidade(Automovel automovel, LocalDate inicio, LocalDate fim, Long ignorarPedidoId) {
        boolean ocupado = pedidoRepository.findByAutomovelId(automovel.getId()).stream()
                .filter(p -> !Objects.equals(p.getId(), ignorarPedidoId))
                .anyMatch(p -> p.conflitaCom(inicio, fim));
        if (ocupado) {
            throw new RegraNegocioException("Automóvel indisponível no período escolhido.");
        }
    }
}
