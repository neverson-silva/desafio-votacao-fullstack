package com.neverson.votacao.services;

import com.neverson.votacao.domain.entities.Sessao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SessaoService {
    Sessao abrirSessao(UUID pautaId, Integer seconds);
    Optional<Sessao> findById(UUID id);

    List<Sessao> buscarSessoes();
}
