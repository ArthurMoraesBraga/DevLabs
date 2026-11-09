package br.pucminas.aluguelcarros.model;

import br.pucminas.aluguelcarros.exception.RegraNegocioException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Pedido de aluguel. As regras de transição de status ficam aqui (modelo rico),
 * e os services apenas orquestram.
 */
@Entity
@Table(name = "pedido_aluguel")
public class PedidoAluguel implements Identificavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "automovel_id", nullable = false)
    private Automovel automovel;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusPedido status = StatusPedido.PENDENTE;

    @Column(length = 2000)
    private String parecer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "agente_avaliador_id")
    private Agente agenteAvaliador;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contrato_id")
    private Contrato contrato;

    public void definirPeriodo(LocalDate inicio, LocalDate fim) {
        if (inicio == null || fim == null) {
            throw new RegraNegocioException("Datas de início e fim são obrigatórias.");
        }
        if (fim.isBefore(inicio)) {
            throw new RegraNegocioException("A data de fim deve ser igual ou posterior à data de início.");
        }
        this.dataInicio = inicio;
        this.dataFim = fim;
    }

    public boolean conflitaCom(LocalDate inicio, LocalDate fim) {
        return status.isAtivo() && !dataFim.isBefore(inicio) && !dataInicio.isAfter(fim);
    }

    /** Cliente só modifica enquanto o pedido ainda não foi avaliado. */
    public void validarModificacaoPeloCliente() {
        if (status != StatusPedido.PENDENTE) {
            throw new RegraNegocioException("Só é possível modificar pedidos com status PENDENTE.");
        }
    }

    /** Agente pode modificar pedidos que ainda não viraram contrato. */
    public void validarModificacaoPeloAgente() {
        if (status != StatusPedido.PENDENTE && status != StatusPedido.APROVADO) {
            throw new RegraNegocioException("Só é possível modificar pedidos PENDENTES ou APROVADOS.");
        }
    }

    public void cancelar() {
        if (status != StatusPedido.PENDENTE && status != StatusPedido.APROVADO) {
            throw new RegraNegocioException("Só é possível cancelar pedidos PENDENTES ou APROVADOS.");
        }
        this.status = StatusPedido.CANCELADO;
    }

    /** Análise financeira feita por um agente. */
    public void avaliar(Agente agente, boolean aprovado, String parecer) {
        if (status != StatusPedido.PENDENTE) {
            throw new RegraNegocioException("Só é possível avaliar pedidos com status PENDENTE.");
        }
        this.agenteAvaliador = agente;
        this.parecer = parecer;
        this.status = aprovado ? StatusPedido.APROVADO : StatusPedido.REPROVADO;
    }

    /** Execução do contrato após parecer positivo. */
    public void executarContrato(Contrato contrato) {
        if (status != StatusPedido.APROVADO) {
            throw new RegraNegocioException("O contrato só pode ser executado para pedidos APROVADOS.");
        }
        this.contrato = contrato;
        this.status = StatusPedido.CONTRATADO;
    }

    @Override
    public Long getId() { return id; }

    @Override
    public void setId(Long id) { this.id = id; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Automovel getAutomovel() { return automovel; }
    public void setAutomovel(Automovel automovel) { this.automovel = automovel; }

    public LocalDate getDataInicio() { return dataInicio; }
    public LocalDate getDataFim() { return dataFim; }

    public StatusPedido getStatus() { return status; }

    public String getParecer() { return parecer; }

    public Agente getAgenteAvaliador() { return agenteAvaliador; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }

    public Contrato getContrato() { return contrato; }
}