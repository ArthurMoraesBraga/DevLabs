package br.pucminas.aluguelcarros.repository;

import br.pucminas.aluguelcarros.model.Identificavel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Repositório genérico em memória.
 *
 * Os nomes dos métodos (save, findById, findAll, deleteById, existsById) são
 * os mesmos do Spring Data JPA de propósito: na etapa do banco de dados,
 * basta trocar cada repositório por "interface XRepository extends JpaRepository<X, Long>"
 * e os services continuam funcionando sem alteração.
 */
public abstract class InMemoryRepository<T extends Identificavel> {

    private final Map<Long, T> dados = new ConcurrentHashMap<>();
    private final AtomicLong sequencia = new AtomicLong(0);

    public T save(T entidade) {
        if (entidade.getId() == null) {
            entidade.setId(sequencia.incrementAndGet());
        }
        dados.put(entidade.getId(), entidade);
        return entidade;
    }

    public Optional<T> findById(Long id) {
        return Optional.ofNullable(dados.get(id));
    }

    public List<T> findAll() {
        List<T> lista = new ArrayList<>(dados.values());
        lista.sort(Comparator.comparing(Identificavel::getId));
        return lista;
    }

    public boolean existsById(Long id) {
        return dados.containsKey(id);
    }

    public void deleteById(Long id) {
        dados.remove(id);
    }
}
