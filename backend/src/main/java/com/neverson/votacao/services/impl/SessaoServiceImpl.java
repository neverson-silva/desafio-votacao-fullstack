package com.neverson.votacao.services.impl;

import com.neverson.votacao.domain.entities.Pauta;
import com.neverson.votacao.domain.entities.Sessao;
import com.neverson.votacao.repositories.SessaoRepository;
import com.neverson.votacao.services.PautaService;
import com.neverson.votacao.services.SessaoService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SessaoServiceImpl implements SessaoService {
    private final SessaoRepository sessaoRepository;
    private final PautaService pautaService;

    private final Integer DEFAULT_SESSION_TIME_SECONDS = 60; // 1 minuto

    public SessaoServiceImpl(SessaoRepository sessaoRepository, PautaService pautaService) {
        this.sessaoRepository = sessaoRepository;
        this.pautaService = pautaService;
    }

    @Override
    public Sessao abrirSessao(UUID pautaId, Integer seconds) {
        Pauta pauta = pautaService.findById(pautaId)
                .orElseThrow(() -> new RuntimeException("Pauta não encontrada"));

        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime fim = agora.plusSeconds(seconds != null ? seconds : this.DEFAULT_SESSION_TIME_SECONDS);

        return sessaoRepository.save(new Sessao( pauta, agora, fim));
    }

    @Override
    public Optional<Sessao> findById(UUID id) {
        return this.sessaoRepository.findById(id);
    }

    @Override
    public List<Sessao> buscarSessoes() {
        return this.sessaoRepository.findAll(Sort.by(Sort.Direction.DESC, "fechamento"));
    }
}
