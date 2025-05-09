package com.neverson.votacao.repositories;

import com.neverson.votacao.domain.dtos.ResultadoVotacaoDTO;
import com.neverson.votacao.domain.entities.Pauta;
import com.neverson.votacao.domain.entities.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface VotoRepository extends JpaRepository<Voto, UUID> {
    boolean existsByAssociadoIdAndPauta(String associadoId, Pauta pauta);

    List<Voto> findAllByPauta(Pauta pauta);

    @Query(value = """
        SELECT
            COUNT(*) AS totalVotos,
            SUM(CASE WHEN v.voto = 1 THEN 1 ELSE 0 END) AS votosSim,
            SUM(CASE WHEN v.voto = 0 THEN 1 ELSE 0 END) AS votosNao
        FROM voto v
        WHERE v.pauta_id = :pautaId
    """, nativeQuery = true)
    ResultadoVotacaoDTO obterResultadoVotacao(@Param("pautaId") UUID pautaId);

}
