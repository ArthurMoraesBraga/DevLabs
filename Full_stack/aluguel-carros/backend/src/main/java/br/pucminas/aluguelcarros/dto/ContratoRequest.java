package br.pucminas.aluguelcarros.dto;

import br.pucminas.aluguelcarros.model.TipoProprietario;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/** "credito" é opcional: só é enviado quando o aluguel tem contrato de crédito. */
public record ContratoRequest(
        @NotNull(message = "Informe o proprietário do automóvel") TipoProprietario tipoProprietario,
        @Valid CreditoRequest credito
) {
}
