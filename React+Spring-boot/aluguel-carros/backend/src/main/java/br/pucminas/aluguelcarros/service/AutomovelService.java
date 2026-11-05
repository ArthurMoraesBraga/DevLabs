package br.pucminas.aluguelcarros.service;

import br.pucminas.aluguelcarros.dto.AutomovelRequest;
import br.pucminas.aluguelcarros.exception.RecursoNaoEncontradoException;
import br.pucminas.aluguelcarros.exception.RegraNegocioException;
import br.pucminas.aluguelcarros.model.Automovel;
import br.pucminas.aluguelcarros.model.TipoProprietario;
import br.pucminas.aluguelcarros.repository.AutomovelRepository;
import br.pucminas.aluguelcarros.repository.PedidoAluguelRepository;
import br.pucminas.aluguelcarros.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class AutomovelService {

    private final AutomovelRepository automovelRepository;
    private final PedidoAluguelRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;

    public AutomovelService(AutomovelRepository automovelRepository, PedidoAluguelRepository pedidoRepository,
                            UsuarioRepository usuarioRepository) {
        this.automovelRepository = automovelRepository;
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Automovel> listar() {
        return automovelRepository.findAll();
    }

    public Automovel buscar(Long id) {
        return automovelRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Automóvel " + id + " não encontrado."));
    }

    public Automovel criar(AutomovelRequest req) {
        validar(req, null);
        Automovel a = new Automovel();
        preencher(a, req);
        return automovelRepository.save(a);
    }

    public Automovel atualizar(Long id, AutomovelRequest req) {
        Automovel a = buscar(id);
        validar(req, id);
        preencher(a, req);
        return automovelRepository.save(a);
    }

    public void excluir(Long id) {
        buscar(id);
        if (!pedidoRepository.findByAutomovelId(id).isEmpty()) {
            throw new RegraNegocioException("Automóvel possui pedidos vinculados e não pode ser excluído.");
        }
        automovelRepository.deleteById(id);
    }

    private void validar(AutomovelRequest req, Long idAtual) {
        automovelRepository.findByPlaca(req.placa())
                .filter(a -> !a.getId().equals(idAtual))
                .ifPresent(a -> { throw new RegraNegocioException("Placa já cadastrada."); });
        automovelRepository.findByMatricula(req.matricula())
                .filter(a -> !a.getId().equals(idAtual))
                .ifPresent(a -> { throw new RegraNegocioException("Matrícula já cadastrada."); });

        if ((req.tipoProprietario() == null) != (req.proprietarioId() == null)) {
            throw new RegraNegocioException("Informe tipo e id do proprietário juntos (ou nenhum dos dois).");
        }
        if (req.proprietarioId() != null) {
            var dono = usuarioRepository.findById(req.proprietarioId())
                    .orElseThrow(() -> new RegraNegocioException("Proprietário não encontrado."));
            if (!dono.getPerfil().name().equals(req.tipoProprietario().name())) {
                throw new RegraNegocioException("O proprietário informado não é do tipo " + req.tipoProprietario() + ".");
            }
        }
    }

    private void preencher(Automovel a, AutomovelRequest req) {
        a.setMatricula(req.matricula().trim().toUpperCase(Locale.ROOT));
        a.setAno(req.ano());
        a.setMarca(req.marca().trim());
        a.setModelo(req.modelo().trim());
        a.setPlaca(req.placa().trim().toUpperCase(Locale.ROOT));
        a.setTipoProprietario(req.tipoProprietario());
        a.setProprietarioId(req.proprietarioId());
    }

    /** Usado pelo contrato para registrar a propriedade do automóvel. */
    void registrarPropriedade(Automovel a, TipoProprietario tipo, Long proprietarioId) {
        a.setTipoProprietario(tipo);
        a.setProprietarioId(proprietarioId);
        automovelRepository.save(a);
    }
}
