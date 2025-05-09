package com.neverson.votacao.domain.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ResultadoVotacaoDTO implements Serializable {
    private Integer totalVotos;
    private Integer votosSim;
    private Integer votosNao;

    public ResultadoVotacaoDTO(Integer totalVotos, Integer votosSim, Integer votosNao) {
        this.totalVotos = totalVotos;
        this.votosSim = votosSim;
        this.votosNao = votosNao;
    }
}
