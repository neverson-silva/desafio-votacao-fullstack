package com.neverson.votacao.services;

import com.neverson.votacao.config.exceptions.HttpException;
import com.neverson.votacao.domain.dtos.ElegibilidadeVotacaoDTO;

public interface VotacaoEligibilidadeService {

    ElegibilidadeVotacaoDTO isEligibleToVote(String cpf) throws HttpException;
}
