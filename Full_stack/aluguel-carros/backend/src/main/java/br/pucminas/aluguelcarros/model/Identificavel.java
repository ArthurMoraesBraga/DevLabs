package br.pucminas.aluguelcarros.model;

/**
 * Contrato mínimo para qualquer entidade que possa ser salva em um repositório.
 * Quando migrar para JPA, o id passa a ser anotado com @Id/@GeneratedValue.
 */
public interface Identificavel {
    Long getId();
    void setId(Long id);
}
