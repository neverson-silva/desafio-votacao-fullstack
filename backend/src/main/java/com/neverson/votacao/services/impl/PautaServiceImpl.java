package com.neverson.votacao.services.impl;

import com.neverson.votacao.domain.entities.Pauta;
import com.neverson.votacao.repositories.PautaRepository;
import com.neverson.votacao.services.PautaService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PautaServiceImpl implements PautaService {
    private final PautaRepository pautaRepository;

    public PautaServiceImpl(PautaRepository pautaRepository) {
        this.pautaRepository = pautaRepository;
    }
    @Override
    public Pauta criarPauta(String titulo, String descricao) {
        return pautaRepository.save(new Pauta(titulo, descricao));
    }

    @Override
    public Optional<Pauta> findById(UUID id) {
        return this.pautaRepository.findById(id);
    }

    @Override
    public List<Pauta> buscarPautas() {
        return pautaRepository.buscarPautas();
    }
}
