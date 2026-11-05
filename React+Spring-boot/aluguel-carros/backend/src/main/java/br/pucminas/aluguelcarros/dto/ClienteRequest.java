package br.pucminas.aluguelcarros.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Usado no cadastro e na atualização de clientes.
 * Na atualização, "senha" é opcional (em branco = mantém a atual).
 */
public record ClienteRequest(
        @NotBlank(message = "Informe o usuário") @Size(min = 3, max = 50) String login,
        String senha,
        @NotBlank(message = "Informe o nome") String nome,
        @NotBlank(message = "Informe o endereço") String endereco,
        @NotBlank(message = "Informe o RG") String rg,
        @NotBlank(message = "Informe o CPF")
        @Pattern(regexp = "\\d{11}", message = "CPF deve ter 11 dígitos (apenas números)") String cpf,
        @NotBlank(message = "Informe a profissão") String profissao,
        @Size(max = 3, message = "Máximo de 3 rendimentos") List<@Valid RendimentoDTO> rendimentos
) {
}
