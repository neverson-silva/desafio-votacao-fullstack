package com.neverson.votacao.domain.dtos;

import com.neverson.votacao.domain.enums.EStatusEligibilidade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ElegibilidadeVotacaoDTO implements Serializable {
    EStatusEligibilidade status;
}
