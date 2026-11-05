package br.pucminas.aluguelcarros.model;

import br.pucminas.aluguelcarros.exception.RegraNegocioException;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Contratante do aluguel (usuário individual). */
@Entity
@DiscriminatorValue("CLIENTE")
public class Cliente extends Usuario {

    public static final int MAX_RENDIMENTOS = 3;

    @Column(length = 50)
    private String rg;

    @Column(length = 11, unique = true)
    private String cpf;

    private String profissao;

    // Tabela própria "cliente_rendimento" (máximo 3 linhas por cliente)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "cliente_rendimento", joinColumns = @JoinColumn(name = "cliente_id"))
    private List<Rendimento> rendimentos = new ArrayList<>();

    @Override
    public Perfil getPerfil() {
        return Perfil.CLIENTE;
    }

    public String getRg() { return rg; }
    public void setRg(String rg) { this.rg = rg; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getProfissao() { return profissao; }
    public void setProfissao(String profissao) { this.profissao = profissao; }

    public List<Rendimento> getRendimentos() {
        return Collections.unmodifiableList(rendimentos);
    }

    /** Substitui os rendimentos, respeitando a regra de no máximo 3. */
    public void setRendimentos(List<Rendimento> novos) {
        if (novos != null && novos.size() > MAX_RENDIMENTOS) {
            throw new RegraNegocioException("Um cliente pode ter no máximo " + MAX_RENDIMENTOS + " rendimentos.");
        }
        rendimentos.clear();
        if (novos != null) {
            rendimentos.addAll(novos);
        }
    }

    public BigDecimal getRendaTotal() {
        return rendimentos.stream()
                .map(Rendimento::getValor)
                .filter(v -> v != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}