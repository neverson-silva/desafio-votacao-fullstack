package com.neverson.votacao.services.impl;

import com.neverson.votacao.config.exceptions.HttpException;
import com.neverson.votacao.domain.dtos.ResultadoVotacaoDTO;

import com.neverson.votacao.domain.entities.Voto;
import com.neverson.votacao.domain.enums.EVotoOpcao;

import com.neverson.votacao.repositories.VotoRepository;
import com.neverson.votacao.services.SessaoService;
import com.neverson.votacao.services.VotacaoService;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class VotacaoServiceImpl implements VotacaoService {

    private final VotoRepository votoRepository;
    private final SessaoService sessaoService;

    public VotacaoServiceImpl(VotoRepository votoRepository, SessaoService sessaoService) {

        this.votoRepository = votoRepository;
        this.sessaoService = sessaoService;
    }

    @Override
    public Voto votar(UUID sessaoId, String cpf, EVotoOpcao voto) {
        var sessao = sessaoService.findById(sessaoId).orElseThrow(() -> HttpException.notFound("Sessão não encontrada"));

        if (LocalDateTime.now().isAfter(sessao.getFechamento())) {
            throw HttpException.badRequest("Sessão de votação encerrada");
        }

        var votoExistente = votoRepository.existsByAssociadoIdAndPauta(cpf, sessao.getPauta());

        if (votoExistente) {
            throw HttpException.conflict("Associado já votou nesta pauta");
        }

        var novoVoto = new Voto(cpf, voto, sessao.getPauta());

        return votoRepository.save(novoVoto);
    }

    @Override
    public ResultadoVotacaoDTO resultado(UUID pautaId) {
        return votoRepository.obterResultadoVotacao(pautaId);
    }
}
