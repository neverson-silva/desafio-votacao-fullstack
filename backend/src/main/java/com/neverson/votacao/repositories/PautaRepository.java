package com.neverson.votacao.repositories;

import com.neverson.votacao.domain.entities.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;


public interface PautaRepository extends JpaRepository<Pauta, UUID> {

    @Query("""
    from Pauta pauta
    left join fetch pauta.sessao sessao
    order by coalesce(sessao.id, pauta.id) desc
    limit 10
    """)
    List<Pauta> buscarPautas();
}
