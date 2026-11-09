package br.pucminas.aluguelcarros.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

/** Agente que modifica e avalia pedidos (empresa ou banco). */
@Entity
public abstract class Agente extends Usuario {

    @Column(length = 14)
    private String cnpj;

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
}