package br.pucminas.aluguelcarros.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

/** Contrato de aluguel gerado a partir de um pedido aprovado. */
@Entity
@Table(name = "contrato")
public class Contrato implements Identificavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pedido_id")
    private Long pedidoId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_proprietario", length = 20)
    private TipoProprietario tipoProprietario;

    @Column(name = "data_assinatura")
    private LocalDate dataAssinatura = LocalDate.now();

    @Embedded
    private ContratoCredito contratoCredito; // opcional

    @Override
    public Long getId() { return id; }

    @Override
    public void setId(Long id) { this.id = id; }

    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }

    public TipoProprietario getTipoProprietario() { return tipoProprietario; }
    public void setTipoProprietario(TipoProprietario tipoProprietario) { this.tipoProprietario = tipoProprietario; }

    public LocalDate getDataAssinatura() { return dataAssinatura; }

    public ContratoCredito getContratoCredito() { return contratoCredito; }
    public void setContratoCredito(ContratoCredito contratoCredito) { this.contratoCredito = contratoCredito; }
}