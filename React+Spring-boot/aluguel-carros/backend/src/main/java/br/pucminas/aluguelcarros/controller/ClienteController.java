package br.pucminas.aluguelcarros.controller;

import br.pucminas.aluguelcarros.dto.ClienteRequest;
import br.pucminas.aluguelcarros.dto.ClienteResponse;
import br.pucminas.aluguelcarros.model.Usuario;
import br.pucminas.aluguelcarros.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** CRUD de cliente. POST é público (cadastro prévio); o resto exige login. */
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponse criar(@RequestBody @Valid ClienteRequest req) {
        return ClienteResponse.from(clienteService.criar(req));
    }

    @GetMapping
    @PreAuthorize("hasRole('AGENTE')")
    public List<ClienteResponse> listar() {
        return clienteService.listar().stream().map(ClienteResponse::from).toList();
    }

    @GetMapping("/{id}")
    public ClienteResponse buscar(@PathVariable Long id, @AuthenticationPrincipal Usuario logado) {
        return ClienteResponse.from(clienteService.buscar(id, logado));
    }

    @PutMapping("/{id}")
    public ClienteResponse atualizar(@PathVariable Long id, @RequestBody @Valid ClienteRequest req,
                                     @AuthenticationPrincipal Usuario logado) {
        return ClienteResponse.from(clienteService.atualizar(id, req, logado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id, @AuthenticationPrincipal Usuario logado) {
        clienteService.excluir(id, logado);
        return ResponseEntity.noContent().build();
    }
}
