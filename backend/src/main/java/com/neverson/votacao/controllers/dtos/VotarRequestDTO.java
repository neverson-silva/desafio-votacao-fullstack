package com.neverson.votacao.controllers.dtos;

import com.neverson.votacao.domain.enums.EVotoOpcao;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VotarRequestDTO implements Serializable {

    private java.util.UUID sessaoId;

    @CPF
    private String cpf;

    @Enumerated(EnumType.STRING)
    private EVotoOpcao voto;
}
