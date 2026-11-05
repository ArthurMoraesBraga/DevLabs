package br.pucminas.aluguelcarros.dto;

import br.pucminas.aluguelcarros.model.TipoProprietario;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AutomovelRequest(
        @NotBlank(message = "Informe a matrícula") String matricula,
        @NotNull(message = "Informe o ano") @Min(1950) @Max(2100) Integer ano,
        @NotBlank(message = "Informe a marca") String marca,
        @NotBlank(message = "Informe o modelo") String modelo,
        @NotBlank(message = "Informe a placa") String placa,
        TipoProprietario tipoProprietario,
        Long proprietarioId
) {
}
