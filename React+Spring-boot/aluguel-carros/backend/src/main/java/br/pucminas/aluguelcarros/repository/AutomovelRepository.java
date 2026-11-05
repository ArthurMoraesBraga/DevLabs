package br.pucminas.aluguelcarros.repository;

import br.pucminas.aluguelcarros.model.Automovel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutomovelRepository extends JpaRepository<Automovel, Long> {

    @Override
    @Query("select a from Automovel a order by a.id")
    List<Automovel> findAll();

    @Query("select a from Automovel a where lower(a.placa) = lower(:placa)")
    Optional<Automovel> findByPlaca(@Param("placa") String placa);

    @Query("select a from Automovel a where lower(a.matricula) = lower(:matricula)")
    Optional<Automovel> findByMatricula(@Param("matricula") String matricula);
}