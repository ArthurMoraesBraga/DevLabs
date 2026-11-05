package br.pucminas.aluguelcarros.dto;

import br.pucminas.aluguelcarros.model.Rendimento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record RendimentoDTO(
        @NotBlank(message = "Informe a entidade empregadora") String entidadeEmpregadora,
        @NotNull(message = "Informe o valor") @Positive(message = "O valor deve ser positivo") BigDecimal valor
) {

    public Rendimento toModel() {
        return new Rendimento(entidadeEmpregadora, valor);
    }

    public static RendimentoDTO from(Rendimento r) {
        return new RendimentoDTO(r.getEntidadeEmpregadora(), r.getValor());
    }
}
