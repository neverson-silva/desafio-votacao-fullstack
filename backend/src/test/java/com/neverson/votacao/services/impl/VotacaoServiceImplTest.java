package com.neverson.votacao.services.impl;

import com.neverson.votacao.config.exceptions.HttpException;
import com.neverson.votacao.domain.dtos.ResultadoVotacaoDTO;
import com.neverson.votacao.domain.entities.Pauta;
import com.neverson.votacao.domain.entities.Sessao;
import com.neverson.votacao.domain.entities.Voto;
import com.neverson.votacao.domain.enums.EVotoOpcao;
import com.neverson.votacao.repositories.VotoRepository;
import com.neverson.votacao.services.SessaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VotacaoServiceImplTest {

    private VotacaoServiceImpl votacaoService;

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private SessaoService sessaoService;

    @BeforeEach
    void configurarAmbienteDeTeste() {
        votacaoService = new VotacaoServiceImpl(votoRepository, sessaoService);
    }

    @Test
    @DisplayName("Deve registrar um voto com sucesso quando todos os critérios são atendidos")
    void testVotar_CriteriosAtendidos_DeveRegistrarVoto() {
        UUID sessaoId = UUID.randomUUID();
        String cpf = "12345678901";
        EVotoOpcao opcaoVoto = EVotoOpcao.SIM;
        
        Pauta pauta = new Pauta("Pauta de Teste", "Descrição da Pauta");
        Sessao sessao = new Sessao(pauta, LocalDateTime.now(), LocalDateTime.now().plusMinutes(10));
        Voto votoEsperado = new Voto(cpf, opcaoVoto, pauta);

        when(sessaoService.findById(sessaoId)).thenReturn(Optional.of(sessao));
        when(votoRepository.existsByAssociadoIdAndPauta(cpf, pauta)).thenReturn(false);
        when(votoRepository.save(any(Voto.class))).thenReturn(votoEsperado);

        Voto votoRegistrado = votacaoService.votar(sessaoId, cpf, opcaoVoto);

        assertNotNull(votoRegistrado);
        assertEquals(cpf, votoRegistrado.getAssociadoId());
        assertEquals(opcaoVoto, votoRegistrado.getVoto());
        assertEquals(pauta, votoRegistrado.getPauta());

        verify(sessaoService).findById(sessaoId);
        verify(votoRepository).existsByAssociadoIdAndPauta(cpf, pauta);
        verify(votoRepository).save(any(Voto.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando a sessão não for encontrada")
    void testVotar_SessaoNaoEncontrada_DeveLancarExcecao() {
        UUID sessaoId = UUID.randomUUID();
        String cpf = "12345678901";
        EVotoOpcao opcaoVoto = EVotoOpcao.SIM;

        when(sessaoService.findById(sessaoId)).thenReturn(Optional.empty());

        HttpException exception = assertThrows(HttpException.class,
            () -> votacaoService.votar(sessaoId, cpf, opcaoVoto)
        );

        assertEquals("Sessão não encontrada", exception.getMessage());
        verify(sessaoService).findById(sessaoId);
        verify(votoRepository, never()).save(any(Voto.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando a sessão de votação já estiver encerrada")
    void testVotar_SessaoEncerrada_DeveLancarExcecao() {
        UUID sessaoId = UUID.randomUUID();
        String cpf = "12345678901";
        EVotoOpcao opcaoVoto = EVotoOpcao.SIM;
        
        Pauta pauta = new Pauta("Pauta de Teste", "Descrição da Pauta");
        Sessao sessao = new Sessao(pauta, LocalDateTime.now().minusMinutes(15), LocalDateTime.now().minusMinutes(5));

        when(sessaoService.findById(sessaoId)).thenReturn(Optional.of(sessao));

        HttpException exception = assertThrows(HttpException.class,
            () -> votacaoService.votar(sessaoId, cpf, opcaoVoto)
        );

        assertEquals("Sessão de votação encerrada", exception.getMessage());
        verify(sessaoService).findById(sessaoId);
        verify(votoRepository, never()).save(any(Voto.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o associado já votou na pauta")
    void testVotar_AssociadoJaVotou_DeveLancarExcecao() {
        UUID sessaoId = UUID.randomUUID();
        String cpf = "12345678901";
        EVotoOpcao opcaoVoto = EVotoOpcao.SIM;
        
        Pauta pauta = new Pauta("Pauta de Teste", "Descrição da Pauta");
        Sessao sessao = new Sessao(pauta, LocalDateTime.now(), LocalDateTime.now().plusMinutes(10));

        when(sessaoService.findById(sessaoId)).thenReturn(Optional.of(sessao));
        when(votoRepository.existsByAssociadoIdAndPauta(cpf, pauta)).thenReturn(true);

        HttpException exception = assertThrows(HttpException.class,
            () -> votacaoService.votar(sessaoId, cpf, opcaoVoto)
        );

        assertEquals("Associado já votou nesta pauta", exception.getMessage());
        verify(sessaoService).findById(sessaoId);
        verify(votoRepository).existsByAssociadoIdAndPauta(cpf, pauta);
        verify(votoRepository, never()).save(any(Voto.class));
    }

    @Test
    @DisplayName("Deve obter o resultado da votação para uma pauta")
    void testResultado_DeveRetornarResultadoVotacao() {
        UUID pautaId = UUID.randomUUID();
        ResultadoVotacaoDTO resultadoEsperado = new ResultadoVotacaoDTO(10, 5, 5);

        when(votoRepository.obterResultadoVotacao(pautaId)).thenReturn(resultadoEsperado);

        ResultadoVotacaoDTO resultadoObtido = votacaoService.resultado(pautaId);

        assertNotNull(resultadoObtido);
        assertEquals(resultadoEsperado.getVotosSim(), resultadoObtido.getVotosSim());
        assertEquals(resultadoEsperado.getVotosNao(), resultadoObtido.getVotosNao());

        verify(votoRepository).obterResultadoVotacao(pautaId);
    }
}
