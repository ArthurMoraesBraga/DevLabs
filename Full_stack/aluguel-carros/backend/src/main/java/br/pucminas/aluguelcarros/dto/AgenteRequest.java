package br.pucminas.aluguelcarros.dto;

import br.pucminas.aluguelcarros.model.Perfil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AgenteRequest(
        @NotBlank(message = "Informe o usuário") @Size(min = 3, max = 50) String login,
        @NotBlank(message = "Informe a senha") @Size(min = 6, message = "A senha deve ter ao menos 6 caracteres") String senha,
        @NotBlank(message = "Informe o nome") String nome,
        @NotBlank(message = "Informe o endereço") String endereco,
        @NotBlank(message = "Informe o CNPJ")
        @Pattern(regexp = "\\d{14}", message = "CNPJ deve ter 14 dígitos (apenas números)") String cnpj,
        @NotNull(message = "Informe o tipo (EMPRESA ou BANCO)") Perfil tipo
) {
}
