package br.pucminas.aluguelcarros.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record CreditoRequest(
        @NotNull(message = "Escolha o banco") Long bancoId,
        @NotNull(message = "Informe o valor") @Positive BigDecimal valor,
        @NotNull(message = "Informe a taxa de juros") @PositiveOrZero BigDecimal taxaJurosMensal,
        @NotNull(message = "Informe o prazo") @Min(1) Integer prazoMeses
) {
}
