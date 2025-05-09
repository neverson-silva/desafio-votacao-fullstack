package com.neverson.votacao.controllers.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AbrirSessaoRequestDTO {
    private Integer duracao = 60;
}
