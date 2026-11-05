package br.pucminas.aluguelcarros.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/** Parecer financeiro do agente sobre um pedido. */
public record AvaliacaoRequest(
        @NotNull(message = "Informe se o pedido foi aprovado") Boolean aprovado,
        @NotBlank(message = "Escreva o parecer") String parecer
) {
}
