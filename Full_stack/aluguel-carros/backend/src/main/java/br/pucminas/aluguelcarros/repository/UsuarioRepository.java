package br.pucminas.aluguelcarros.repository;

import br.pucminas.aluguelcarros.model.Agente;
import br.pucminas.aluguelcarros.model.Cliente;
import br.pucminas.aluguelcarros.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/** Guarda clientes e agentes juntos (tabela "usuario"), pois o login precisa ser único entre todos. */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("select u from Usuario u where lower(u.login) = lower(:login)")
    Optional<Usuario> findByLogin(@Param("login") String login);

    default boolean existsByLogin(String login) {
        return findByLogin(login).isPresent();
    }

    @Query("select c from Cliente c order by c.id")
    List<Cliente> findAllClientes();

    @Query("select a from Agente a order by a.id")
    List<Agente> findAllAgentes();

    @Query("select c from Cliente c where c.cpf = :cpf")
    Optional<Cliente> findClienteByCpf(@Param("cpf") String cpf);
}