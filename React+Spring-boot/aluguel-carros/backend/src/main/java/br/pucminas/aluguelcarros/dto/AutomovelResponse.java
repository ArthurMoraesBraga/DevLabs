package br.pucminas.aluguelcarros.dto;

import br.pucminas.aluguelcarros.model.Automovel;
import br.pucminas.aluguelcarros.model.TipoProprietario;

public record AutomovelResponse(
        Long id, String matricula, Integer ano, String marca, String modelo, String placa,
        String imagemUrl, TipoProprietario tipoProprietario, Long proprietarioId
) {

    public static AutomovelResponse from(Automovel a) {
        return new AutomovelResponse(a.getId(), a.getMatricula(), a.getAno(), a.getMarca(), a.getModelo(),
                a.getPlaca(), a.getImagemUrl(), a.getTipoProprietario(), a.getProprietarioId());
    }
}
