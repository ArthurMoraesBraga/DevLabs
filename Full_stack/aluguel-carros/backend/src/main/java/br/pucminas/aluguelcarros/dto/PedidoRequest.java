package br.pucminas.aluguelcarros.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PedidoRequest(
        @NotNull(message = "Escolha um automóvel") Long automovelId,
        @NotNull(message = "Informe a data de início")
        @FutureOrPresent(message = "A data de início não pode estar no passado") LocalDate dataInicio,
        @NotNull(message = "Informe a data de fim") LocalDate dataFim
) {
}
