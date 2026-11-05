package br.pucminas.aluguelcarros.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/** Banco agente: além de avaliar pedidos, pode conceder contratos de crédito. */
@Entity
@DiscriminatorValue("BANCO")
public class Banco extends Agente {

    @Override
    public Perfil getPerfil() {
        return Perfil.BANCO;
    }
}