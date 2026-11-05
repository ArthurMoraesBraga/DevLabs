package br.pucminas.aluguelcarros.dto;

import br.pucminas.aluguelcarros.model.Cliente;
import br.pucminas.aluguelcarros.model.PedidoAluguel;
import br.pucminas.aluguelcarros.model.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record PedidoResponse(
        Long id,
        StatusPedido status,
        LocalDate dataInicio,
        LocalDate dataFim,
        LocalDateTime dataCriacao,
        String parecer,
        ClienteResumo cliente,
        AutomovelResponse automovel,
        UsuarioResponse agenteAvaliador,
        ContratoResponse contrato
) {

    /** Dados do cliente que o agente precisa para a análise financeira. */
    public record ClienteResumo(Long id, String nome, String cpf, String profissao, BigDecimal rendaTotal) {
        static ClienteResumo from(Cliente c) {
            return new ClienteResumo(c.getId(), c.getNome(), c.getCpf(), c.getProfissao(), c.getRendaTotal());
        }
    }

    public static PedidoResponse from(PedidoAluguel p) {
        return new PedidoResponse(
                p.getId(), p.getStatus(), p.getDataInicio(), p.getDataFim(), p.getDataCriacao(), p.getParecer(),
                ClienteResumo.from(p.getCliente()),
                AutomovelResponse.from(p.getAutomovel()),
                UsuarioResponse.from(p.getAgenteAvaliador()),
                ContratoResponse.from(p.getContrato()));
    }
}
