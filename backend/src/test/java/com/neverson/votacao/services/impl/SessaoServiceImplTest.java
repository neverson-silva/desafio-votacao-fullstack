package com.neverson.votacao.services.impl;

import com.neverson.votacao.domain.entities.Pauta;
import com.neverson.votacao.domain.entities.Sessao;
import com.neverson.votacao.repositories.SessaoRepository;
import com.neverson.votacao.services.PautaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessaoServiceImplTest {

    private SessaoServiceImpl sessaoService;

    @Mock
    private SessaoRepository sessaoRepository;

    @Mock
    private PautaService pautaService;

    @BeforeEach
    void configurarAmbienteDeTeste() {
        sessaoService = new SessaoServiceImpl(sessaoRepository, pautaService);
    }

    @Test
    @DisplayName("Deve abrir sessão com tempo padrão quando nenhum tempo for especificado")
    void testAbrirSessao_ComTempoNulo_DeveCriarSessaoComTempadoPadrao() {
        UUID pautaId = UUID.randomUUID();
        Pauta pauta = new Pauta("Pauta de Teste", "Descrição da Pauta");
        Sessao sessaoEsperada = new Sessao(pauta, LocalDateTime.now(), LocalDateTime.now().plusSeconds(60));

        when(pautaService.findById(pautaId)).thenReturn(Optional.of(pauta));
        when(sessaoRepository.save(any(Sessao.class))).thenReturn(sessaoEsperada);

        Sessao sessaoCriada = sessaoService.abrirSessao(pautaId, null);

        assertNotNull(sessaoCriada);
        assertEquals(pauta, sessaoCriada.getPauta());
        assertEquals(60, Duration.between(sessaoCriada.getAbertura(), sessaoCriada.getFechamento()).getSeconds());

        verify(pautaService).findById(pautaId);
        verify(sessaoRepository).save(any(Sessao.class));
    }

    @Test
    @DisplayName("Deve abrir sessão com tempo personalizado quando tempo for especificado")
    void testAbrirSessao_ComTempoPersonalizado_DeveCriarSessaoComTempoEspecificado() {
        UUID pautaId = UUID.randomUUID();
        Pauta pauta = new Pauta("Pauta de Teste", "Descrição da Pauta");
        int tempoPersonalizado = 120; // 2 minutos
        Sessao sessaoEsperada = new Sessao(pauta, LocalDateTime.now(), LocalDateTime.now().plusSeconds(tempoPersonalizado));

        when(pautaService.findById(pautaId)).thenReturn(Optional.of(pauta));
        when(sessaoRepository.save(any(Sessao.class))).thenReturn(sessaoEsperada);

        Sessao sessaoCriada = sessaoService.abrirSessao(pautaId, tempoPersonalizado);

        assertNotNull(sessaoCriada);
        assertEquals(pauta, sessaoCriada.getPauta());
        assertEquals(tempoPersonalizado, Duration.between(sessaoCriada.getAbertura(), sessaoCriada.getFechamento()).getSeconds());

        verify(pautaService).findById(pautaId);
        verify(sessaoRepository).save(any(Sessao.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando pauta não for encontrada")
    void testAbrirSessao_PautaNaoEncontrada_DeveLancarExcecao() {
        UUID pautaId = UUID.randomUUID();

        when(pautaService.findById(pautaId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
            () -> sessaoService.abrirSessao(pautaId, null), 
            "Pauta não encontrada"
        );

        verify(pautaService).findById(pautaId);
        verify(sessaoRepository, never()).save(any(Sessao.class));
    }

    @Test
    @DisplayName("Deve buscar sessão por ID com sucesso")
    void testFindById_SessaoExistente_DeveRetornarSessao() {
        UUID sessaoId = UUID.randomUUID();
        Pauta pauta = new Pauta("Pauta de Teste", "Descrição da Pauta");
        Sessao sessaoEsperada = new Sessao(pauta, LocalDateTime.now(), LocalDateTime.now().plusSeconds(60));

        when(sessaoRepository.findById(sessaoId)).thenReturn(Optional.of(sessaoEsperada));

        Optional<Sessao> sessaoEncontrada = sessaoService.findById(sessaoId);

        assertTrue(sessaoEncontrada.isPresent());
        assertEquals(sessaoEsperada, sessaoEncontrada.get());
        
        verify(sessaoRepository).findById(sessaoId);
    }

    @Test
    @DisplayName("Deve buscar todas as sessões ordenadas por data de fechamento")
    void testBuscarSessoes_DeveBuscarSessoesOrdenadasPorFechamento() {
        Pauta pauta1 = new Pauta("Pauta 1", "Descrição 1");
        Pauta pauta2 = new Pauta("Pauta 2", "Descrição 2");
        
        Sessao sessao1 = new Sessao(pauta1, LocalDateTime.now(), LocalDateTime.now().plusSeconds(60));
        Sessao sessao2 = new Sessao(pauta2, LocalDateTime.now(), LocalDateTime.now().plusSeconds(120));
        
        List<Sessao> sessoesEsperadas = Arrays.asList(sessao1, sessao2);

        when(sessaoRepository.findAll(Sort.by(Sort.Direction.DESC, "fechamento")))
            .thenReturn(sessoesEsperadas);

        List<Sessao> sessoesEncontradas = sessaoService.buscarSessoes();

        assertNotNull(sessoesEncontradas);
        assertEquals(2, sessoesEncontradas.size());
        assertEquals(sessoesEsperadas, sessoesEncontradas);
        
        verify(sessaoRepository).findAll(Sort.by(Sort.Direction.DESC, "fechamento"));
    }
}
