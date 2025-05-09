package com.neverson.votacao.controllers;

import com.neverson.votacao.controllers.dtos.VotarRequestDTO;
import com.neverson.votacao.domain.dtos.ResultadoVotacaoDTO;
import com.neverson.votacao.services.VotacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/votos")
@Tag(name = "Votação para pauta", description = "API para gerenciamento de votos em pautas")
public class VotoController {

    private VotacaoService votacaoService;

    public VotoController(VotacaoService votacaoService) {
        this.votacaoService = votacaoService;
    }

    @PostMapping("")
    @Operation(summary = "Registrar voto", description = "Registra um voto de um associado em uma sessão de votação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Voto registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou associado já votou nesta sessão", content = @Content),
            @ApiResponse(responseCode = "404", description = "Sessão de votação não encontrada", content = @Content),
            @ApiResponse(responseCode = "409", description = "Cooperado já votou", content = @Content)
    })
    public ResponseEntity<Void> votar(
            @RequestBody @Validated
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados do voto", required = true,
                    content = @Content(schema = @Schema(implementation = VotarRequestDTO.class)))
            VotarRequestDTO body) {
        votacaoService.votar(body.getSessaoId(), body.getCpf(), body.getVoto());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pautas/{id}/resultado")
    @Operation(summary = "Obter resultado da votação", description = "Retorna o resultado da votação para uma pauta específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultado da votação obtido com sucesso",
                    content = @Content(schema = @Schema(implementation = ResultadoVotacaoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Pauta não encontrada ou sem votação finalizada", content = @Content)
    })
    public ResponseEntity<ResultadoVotacaoDTO> resultado(
            @Parameter(description = "ID da pauta", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.ok(votacaoService.resultado(id));
    }
}
