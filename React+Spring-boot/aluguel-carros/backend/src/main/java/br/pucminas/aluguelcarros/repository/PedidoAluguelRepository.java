package br.pucminas.aluguelcarros.repository;

import br.pucminas.aluguelcarros.model.PedidoAluguel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoAluguelRepository extends JpaRepository<PedidoAluguel, Long> {

    @Override
    @Query("select p from PedidoAluguel p order by p.id")
    List<PedidoAluguel> findAll();

    @Query("select p from PedidoAluguel p where p.cliente.id = :clienteId order by p.id")
    List<PedidoAluguel> findByClienteId(@Param("clienteId") Long clienteId);

    @Query("select p from PedidoAluguel p where p.automovel.id = :automovelId order by p.id")
    List<PedidoAluguel> findByAutomovelId(@Param("automovelId") Long automovelId);
}