package com.neverson.votacao.services.impl;

import com.neverson.votacao.config.exceptions.HttpException;
import com.neverson.votacao.domain.dtos.ElegibilidadeVotacaoDTO;
import com.neverson.votacao.domain.enums.EStatusEligibilidade;
import com.neverson.votacao.services.VotacaoEligibilidadeService;
import com.neverson.votacao.utils.CPFUtils;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class VotacaoEligibilidadeServiceImpl  implements VotacaoEligibilidadeService {

    private Random random;

    public VotacaoEligibilidadeServiceImpl() {
        this.random = createRandom();
    }

    protected Random createRandom() {
        return new Random();
    }

    @Override
    public ElegibilidadeVotacaoDTO isEligibleToVote(String cpf) throws HttpException {
        if (!CPFUtils.isValid(cpf)) {
            throw HttpException.notFound("CPF inválido");
        }
        boolean canVote = random.nextBoolean();
        return new ElegibilidadeVotacaoDTO(canVote ? EStatusEligibilidade.ABLE_TO_VOTE : EStatusEligibilidade.UNABLE_TO_VOTE);
    }
}
