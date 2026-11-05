package br.pucminas.aluguelcarros.controller;

import br.pucminas.aluguelcarros.dto.AvaliacaoRequest;
import br.pucminas.aluguelcarros.dto.ContratoRequest;
import br.pucminas.aluguelcarros.dto.PedidoRequest;
import br.pucminas.aluguelcarros.dto.PedidoResponse;
import br.pucminas.aluguelcarros.model.Usuario;
import br.pucminas.aluguelcarros.service.ContratoService;
import br.pucminas.aluguelcarros.service.PedidoAluguelService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Cliente: introduz, consulta, modifica e cancela.
 * Agente: consulta, modifica, avalia e executa contrato.
 */
@RestController
@RequestMapping("/api/pedidos")
public class PedidoAluguelController {

    private final PedidoAluguelService pedidoService;
    private final ContratoService contratoService;

    public PedidoAluguelController(PedidoAluguelService pedidoService, ContratoService contratoService) {
        this.pedidoService = pedidoService;
        this.contratoService = contratoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('CLIENTE')")
    public PedidoResponse criar(@RequestBody @Valid PedidoRequest req, @AuthenticationPrincipal Usuario logado) {
        return PedidoResponse.from(pedidoService.criar(req, logado));
    }

    @GetMapping
    public List<PedidoResponse> listar(@AuthenticationPrincipal Usuario logado) {
        return pedidoService.listar(logado).stream().map(PedidoResponse::from).toList();
    }

    @GetMapping("/{id}")
    public PedidoResponse buscar(@PathVariable Long id, @AuthenticationPrincipal Usuario logado) {
        return PedidoResponse.from(pedidoService.buscar(id, logado));
    }

    @PutMapping("/{id}")
    public PedidoResponse atualizar(@PathVariable Long id, @RequestBody @Valid PedidoRequest req,
                                    @AuthenticationPrincipal Usuario logado) {
        return PedidoResponse.from(pedidoService.atualizar(id, req, logado));
    }

    @PatchMapping("/{id}/cancelar")
    @PreAuthorize("hasRole('CLIENTE')")
    public PedidoResponse cancelar(@PathVariable Long id, @AuthenticationPrincipal Usuario logado) {
        return PedidoResponse.from(pedidoService.cancelar(id, logado));
    }

    @PatchMapping("/{id}/avaliar")
    @PreAuthorize("hasRole('AGENTE')")
    public PedidoResponse avaliar(@PathVariable Long id, @RequestBody @Valid AvaliacaoRequest req,
                                  @AuthenticationPrincipal Usuario logado) {
        return PedidoResponse.from(pedidoService.avaliar(id, req, logado));
    }

    @PostMapping("/{id}/contrato")
    @PreAuthorize("hasRole('AGENTE')")
    public PedidoResponse executarContrato(@PathVariable Long id, @RequestBody @Valid ContratoRequest req,
                                           @AuthenticationPrincipal Usuario logado) {
        return PedidoResponse.from(contratoService.executar(id, req, logado));
    }
}
