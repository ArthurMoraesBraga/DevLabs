package br.pucminas.aluguelcarros.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

/** Rendimento auferido por um cliente em uma entidade empregadora. */
@Embeddable
public class Rendimento {

    @Column(name = "entidade_empregadora")
    private String entidadeEmpregadora;

    @Column(name = "valor", precision = 12, scale = 2)
    private BigDecimal valor;

    public Rendimento() {
    }

    public Rendimento(String entidadeEmpregadora, BigDecimal valor) {
        this.entidadeEmpregadora = entidadeEmpregadora;
        this.valor = valor;
    }

    public String getEntidadeEmpregadora() { return entidadeEmpregadora; }
    public void setEntidadeEmpregadora(String entidadeEmpregadora) { this.entidadeEmpregadora = entidadeEmpregadora; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
}