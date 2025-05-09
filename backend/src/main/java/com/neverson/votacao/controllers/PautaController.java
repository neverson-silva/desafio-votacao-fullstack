package com.neverson.votacao.controllers;


import com.neverson.votacao.controllers.dtos.AbrirSessaoRequestDTO;
import com.neverson.votacao.domain.entities.Pauta;
import com.neverson.votacao.domain.entities.Sessao;
import com.neverson.votacao.services.PautaService;
import com.neverson.votacao.services.SessaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/pautas")
@Tag(name = "Pautas", description = "API para gerenciamento de pautas de votação")
public class PautaController {

    private PautaService pautaService;
    private SessaoService sessaoService;

    public PautaController(PautaService pautaService, SessaoService sessaoService) {
        this.pautaService = pautaService;
        this.sessaoService = sessaoService;
    }


    @Operation(summary = "Criar uma nova pauta", description = "Cria uma nova pauta para votação com título e descrição")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pauta criada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pauta.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("")
    public ResponseEntity<Pauta> criarPauta(
            @Parameter(description = "Dados da pauta a ser criada", required = true)
            @RequestBody Pauta pauta,
            UriComponentsBuilder uriBuilder) {
        var response = pautaService.criarPauta(pauta.getTitulo(), pauta.getDescricao());

        URI location = uriBuilder.path("/votos/{id}").buildAndExpand(response.getId()).toUri();

        return ResponseEntity.created(location).body(response);
    }

    @Operation(summary = "Abrir sessão de votação", description = "Abre uma sessão de votação para uma pauta específica com duração opcional em minutos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sessão aberta com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Sessao.class))),
            @ApiResponse(responseCode = "404", description = "Pauta não encontrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })

    @PostMapping("/{id}/sessoes")
    public ResponseEntity<Sessao> abrirSessao(
            @Parameter(description = "ID da pauta", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Duração da sessão em minutos (opcional, padrão definido pelo sistema)")
            @RequestBody AbrirSessaoRequestDTO requestDTO) {
        return ResponseEntity.ok(sessaoService.abrirSessao(id, requestDTO.getDuracao()));
    }

    @GetMapping("")
    public ResponseEntity<List<Pauta>> buscarPautas() {
        var sessoes = pautaService.buscarPautas();
        return ResponseEntity.ok(sessoes);
    }

}
