package br.pucminas.aluguelcarros.dto;

import br.pucminas.aluguelcarros.model.Cliente;

import java.math.BigDecimal;
import java.util.List;

public record ClienteResponse(
        Long id,
        String login,
        String nome,
        String endereco,
        String rg,
        String cpf,
        String profissao,
        List<RendimentoDTO> rendimentos,
        BigDecimal rendaTotal
) {

    public static ClienteResponse from(Cliente c) {
        return new ClienteResponse(
                c.getId(), c.getLogin(), c.getNome(), c.getEndereco(),
                c.getRg(), c.getCpf(), c.getProfissao(),
                c.getRendimentos().stream().map(RendimentoDTO::from).toList(),
                c.getRendaTotal());
    }
}
