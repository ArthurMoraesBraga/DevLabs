package br.pucminas.aluguelcarros.service;

import br.pucminas.aluguelcarros.dto.ContratoRequest;
import br.pucminas.aluguelcarros.dto.CreditoRequest;
import br.pucminas.aluguelcarros.exception.RegraNegocioException;
import br.pucminas.aluguelcarros.model.Banco;
import br.pucminas.aluguelcarros.model.Contrato;
import br.pucminas.aluguelcarros.model.ContratoCredito;
import br.pucminas.aluguelcarros.model.Empresa;
import br.pucminas.aluguelcarros.model.PedidoAluguel;
import br.pucminas.aluguelcarros.model.Usuario;
import br.pucminas.aluguelcarros.repository.ContratoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Execução do contrato de um pedido aprovado.
 * Regras de propriedade do automóvel:
 *  - CLIENTE: fica em nome do cliente do pedido;
 *  - EMPRESA: o agente que executa precisa ser uma empresa;
 *  - BANCO: fica em nome do banco que concedeu o crédito (ou do agente, se ele for banco).
 */
@Service
public class ContratoService {

    private final ContratoRepository contratoRepository;
    private final PedidoAluguelService pedidoService;
    private final AgenteService agenteService;
    private final AutomovelService automovelService;

    public ContratoService(ContratoRepository contratoRepository, PedidoAluguelService pedidoService,
                           AgenteService agenteService, AutomovelService automovelService) {
        this.contratoRepository = contratoRepository;
        this.pedidoService = pedidoService;
        this.agenteService = agenteService;
        this.automovelService = automovelService;
    }

    @Transactional
    public PedidoAluguel executar(Long pedidoId, ContratoRequest req, Usuario agente) {
        PedidoAluguel pedido = pedidoService.buscarPorId(pedidoId);

        Contrato contrato = new Contrato();
        contrato.setPedidoId(pedido.getId());
        contrato.setTipoProprietario(req.tipoProprietario());
        if (req.credito() != null) {
            contrato.setContratoCredito(criarCredito(req.credito()));
        }

        Long proprietarioId = switch (req.tipoProprietario()) {
            case CLIENTE -> pedido.getCliente().getId();
            case EMPRESA -> {
                if (!(agente instanceof Empresa)) {
                    throw new RegraNegocioException("Apenas uma empresa pode ficar como proprietária do automóvel.");
                }
                yield agente.getId();
            }
            case BANCO -> {
                if (contrato.getContratoCredito() != null) {
                    yield contrato.getContratoCredito().getBanco().getId();
                }
                if (agente instanceof Banco) {
                    yield agente.getId();
                }
                throw new RegraNegocioException("Para o automóvel ficar com um banco, informe um contrato de crédito.");
            }
        };

        pedido.executarContrato(contrato); // valida status APROVADO
        contratoRepository.save(contrato);
        automovelService.registrarPropriedade(pedido.getAutomovel(), req.tipoProprietario(), proprietarioId);
        return pedidoService.salvar(pedido);
    }

    private ContratoCredito criarCredito(CreditoRequest req) {
        Banco banco = agenteService.buscarBanco(req.bancoId());
        ContratoCredito credito = new ContratoCredito();
        credito.setBanco(banco);
        credito.setValor(req.valor());
        credito.setTaxaJurosMensal(req.taxaJurosMensal());
        credito.setPrazoMeses(req.prazoMeses());
        return credito;
    }
}
