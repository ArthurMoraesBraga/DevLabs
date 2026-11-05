package br.pucminas.aluguelcarros.config;

import br.pucminas.aluguelcarros.dto.AgenteRequest;
import br.pucminas.aluguelcarros.dto.AutomovelRequest;
import br.pucminas.aluguelcarros.dto.ClienteRequest;
import br.pucminas.aluguelcarros.dto.RendimentoDTO;
import br.pucminas.aluguelcarros.model.Perfil;
import br.pucminas.aluguelcarros.repository.AutomovelRepository;
import br.pucminas.aluguelcarros.repository.UsuarioRepository;
import br.pucminas.aluguelcarros.service.AgenteService;
import br.pucminas.aluguelcarros.service.AutomovelService;
import br.pucminas.aluguelcarros.service.ClienteService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Dados de demonstração carregados na inicialização.
 * Agora os dados ficam no Supabase, então o seeder só cria o que ainda NÃO existe
 * (não duplica a cada reinicialização).
 * Usuários: cliente / empresa / banco — todos com senha 123456.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final ClienteService clienteService;
    private final AgenteService agenteService;
    private final AutomovelService automovelService;
    private final UsuarioRepository usuarioRepository;
    private final AutomovelRepository automovelRepository;

    public DataSeeder(ClienteService clienteService, AgenteService agenteService, AutomovelService automovelService,
                      UsuarioRepository usuarioRepository, AutomovelRepository automovelRepository) {
        this.clienteService = clienteService;
        this.agenteService = agenteService;
        this.automovelService = automovelService;
        this.usuarioRepository = usuarioRepository;
        this.automovelRepository = automovelRepository;
    }

    @Override
    public void run(String... args) {
        if (!usuarioRepository.existsByLogin("cliente")) {
            clienteService.criar(new ClienteRequest(
                    "cliente", "123456", "Maria Silva", "Rua da Bahia, 100 - Belo Horizonte/MG",
                    "MG1234567", "12345678901", "Analista de Sistemas",
                    List.of(new RendimentoDTO("Tech LTDA", new BigDecimal("6500.00")),
                            new RendimentoDTO("Freelance", new BigDecimal("1200.00")))));
        }
        if (!usuarioRepository.existsByLogin("empresa")) {
            agenteService.criar(new AgenteRequest("empresa", "123456", "Locadora PUC Rent",
                    "Av. Dom José Gaspar, 500 - BH/MG", "11222333000144", Perfil.EMPRESA));
        }
        if (!usuarioRepository.existsByLogin("banco")) {
            agenteService.criar(new AgenteRequest("banco", "123456", "Banco Minas",
                    "Av. Afonso Pena, 1500 - BH/MG", "55666777000188", Perfil.BANCO));
        }

        if (automovelRepository.count() == 0) {
            automovelService.criar(new AutomovelRequest("MAT-0001", 2023, "Fiat", "Argo", "ABC1D23", null, null));
            automovelService.criar(new AutomovelRequest("MAT-0002", 2022, "Volkswagen", "Polo", "DEF4G56", null, null));
            automovelService.criar(new AutomovelRequest("MAT-0003", 2024, "Toyota", "Corolla", "HIJ7K89", null, null));
        }
    }
}