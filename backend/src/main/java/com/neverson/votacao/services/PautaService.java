package com.neverson.votacao.services;

import com.neverson.votacao.domain.entities.Pauta;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PautaService {

    Pauta criarPauta(String titulo, String descricao);
    Optional<Pauta> findById(UUID id);

    List<Pauta> buscarPautas();
}
