package br.pucminas.aluguelcarros.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("EMPRESA")
public class Empresa extends Agente {

    @Override
    public Perfil getPerfil() {
        return Perfil.EMPRESA;
    }
}