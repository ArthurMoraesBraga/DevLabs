package br.pucminas.aluguelcarros.model;

/** Perfis de acesso do sistema. EMPRESA e BANCO são agentes. */
public enum Perfil {
    CLIENTE,
    EMPRESA,
    BANCO;

    public boolean isAgente() {
        return this == EMPRESA || this == BANCO;
    }
}
