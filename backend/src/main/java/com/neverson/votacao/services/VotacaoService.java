package com.neverson.votacao.services;


import com.neverson.votacao.domain.dtos.ResultadoVotacaoDTO;
import com.neverson.votacao.domain.entities.Voto;
import com.neverson.votacao.domain.enums.EVotoOpcao;

import java.util.UUID;

public interface VotacaoService {

    Voto votar(UUID pautaId, String cpf, EVotoOpcao voto);

    ResultadoVotacaoDTO resultado(UUID pautaId);
}