package br.pucminas.aluguelcarros.controller;

import br.pucminas.aluguelcarros.dto.AgenteRequest;
import br.pucminas.aluguelcarros.dto.AgenteResponse;
import br.pucminas.aluguelcarros.model.Perfil;
import br.pucminas.aluguelcarros.service.AgenteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/agentes")
public class AgenteController {

    private final AgenteService agenteService;

    public AgenteController(AgenteService agenteService) {
        this.agenteService = agenteService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AgenteResponse criar(@RequestBody @Valid AgenteRequest req) {
        return AgenteResponse.from(agenteService.criar(req));
    }

    /** Ex.: GET /api/agentes?tipo=BANCO para listar os bancos (usado no contrato de crédito). */
    @GetMapping
    public List<AgenteResponse> listar(@RequestParam(required = false) Perfil tipo) {
        return agenteService.listar(tipo).stream().map(AgenteResponse::from).toList();
    }
}
