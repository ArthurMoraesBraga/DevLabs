package br.pucminas.aluguelcarros.model;

/**
 * Ciclo de vida do pedido:
 * PENDENTE -> APROVADO -> CONTRATADO
 * PENDENTE -> REPROVADO
 * PENDENTE/APROVADO -> CANCELADO
 */
public enum StatusPedido {
    PENDENTE,
    APROVADO,
    REPROVADO,
    CANCELADO,
    CONTRATADO;

    /** Status que "ocupam" o automóvel no período do pedido. */
    public boolean isAtivo() {
        return this == PENDENTE || this == APROVADO || this == CONTRATADO;
    }
}
