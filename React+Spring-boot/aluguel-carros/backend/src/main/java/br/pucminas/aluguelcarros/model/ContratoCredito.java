package br.pucminas.aluguelcarros.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;

/**
 * Contrato de crédito concedido por um banco agente, associado a um aluguel.
 * Fica embutido na tabela "contrato" (colunas credito_*); sem crédito, ficam nulas.
 */
@Embeddable
public class ContratoCredito {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "credito_banco_id")
    private Banco banco;

    @Column(name = "credito_valor", precision = 14, scale = 2)
    private BigDecimal valor;

    @Column(name = "credito_taxa_juros_mensal", precision = 7, scale = 4)
    private BigDecimal taxaJurosMensal;

    @Column(name = "credito_prazo_meses")
    private Integer prazoMeses;

    public Banco getBanco() { return banco; }
    public void setBanco(Banco banco) { this.banco = banco; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public BigDecimal getTaxaJurosMensal() { return taxaJurosMensal; }
    public void setTaxaJurosMensal(BigDecimal taxaJurosMensal) { this.taxaJurosMensal = taxaJurosMensal; }

    public Integer getPrazoMeses() { return prazoMeses; }
    public void setPrazoMeses(Integer prazoMeses) { this.prazoMeses = prazoMeses; }
}