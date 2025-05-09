package com.neverson.votacao.controllers;

import com.neverson.votacao.config.exceptions.HttpException;
import com.neverson.votacao.domain.dtos.ElegibilidadeVotacaoDTO;
import com.neverson.votacao.services.VotacaoEligibilidadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
@Tag(name = "Elegibilidade", description = "API para verificação de elegibilidade de CPF para votação")
public class ElegibilidadeController {

    private VotacaoEligibilidadeService votacaoEligibilidadeService;

    public ElegibilidadeController(VotacaoEligibilidadeService votacaoEligibilidadeService) {
        this.votacaoEligibilidadeService = votacaoEligibilidadeService;
    }

    @Operation(
            summary = "Verifica elegibilidade de CPF",
            description = "Este endpoint verifica se um determinado CPF está apto a participar de votações no sistema. " +
                    "A verificação é feita através de um serviço que simula uma integração com sistemas externos."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Verificação realizada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ElegibilidadeVotacaoDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "CPF inválido",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor durante a verificação",
                    content = @Content
            )
    })
    @GetMapping("elegibilidade/{cpf}")
    public ResponseEntity<ElegibilidadeVotacaoDTO> isEligibleToVote(@PathVariable("cpf") String cpf) throws HttpException {
        return ResponseEntity.ok(votacaoEligibilidadeService.isEligibleToVote(cpf));
    }
}
