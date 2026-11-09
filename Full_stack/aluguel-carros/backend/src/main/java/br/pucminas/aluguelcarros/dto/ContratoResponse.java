package br.pucminas.aluguelcarros.dto;

import br.pucminas.aluguelcarros.model.Contrato;
import br.pucminas.aluguelcarros.model.ContratoCredito;
import br.pucminas.aluguelcarros.model.TipoProprietario;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContratoResponse(
        Long id,
        TipoProprietario tipoProprietario,
        LocalDate dataAssinatura,
        Credito credito
) {

    public record Credito(Long bancoId, String bancoNome, BigDecimal valor, BigDecimal taxaJurosMensal, Integer prazoMeses) {
    }

    public static ContratoResponse from(Contrato c) {
        if (c == null) {
            return null;
        }
        ContratoCredito cc = c.getContratoCredito();
        Credito credito = cc == null ? null : new Credito(
                cc.getBanco().getId(), cc.getBanco().getNome(),
                cc.getValor(), cc.getTaxaJurosMensal(), cc.getPrazoMeses());
        return new ContratoResponse(c.getId(), c.getTipoProprietario(), c.getDataAssinatura(), credito);
    }
}
