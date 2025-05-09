package com.neverson.votacao.services.impl;

import com.neverson.votacao.domain.entities.Pauta;
import com.neverson.votacao.repositories.PautaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PautaServiceImplTest {

    private PautaServiceImpl pautaService;

    @Mock
    private PautaRepository pautaRepository;

    @BeforeEach
    void setUp() {
        pautaService = new PautaServiceImpl(pautaRepository);
    }

    @Test
    @DisplayName("Deve criar uma nova pauta com título e descrição válidos")
    void shouldSavePauta() {
        String titulo = "Nova pauta para ser votado";
        String descricao = "Pauta descrita";
        Pauta expectedPauta = new Pauta(titulo, descricao);
        
        when(pautaRepository.save(any(Pauta.class))).thenReturn(expectedPauta);

        Pauta result = pautaService.criarPauta(titulo, descricao);

        assertNotNull(result);
        assertEquals(titulo, result.getTitulo());
        assertEquals(descricao, result.getDescricao());
        verify(pautaRepository).save(any(Pauta.class));
    }

    @Test
    @DisplayName("Deve encontrar pauta existente por ID")
    void shouldReturnPautaWhenExistingId() {
        UUID id = UUID.randomUUID();
        Pauta expectedPauta = new Pauta("Nova pauta para ser votado", "Pauta descrita");
        
        when(pautaRepository.findById(id)).thenReturn(Optional.of(expectedPauta));

        Optional<Pauta> result = pautaService.findById(id);

        assertTrue(result.isPresent());
        assertEquals(expectedPauta, result.get());
        verify(pautaRepository).findById(id);
    }

    @Test
    @DisplayName("Deve retornar Optional vazio para ID de pauta não existente")
    void shouldReturnEmptyOptionalWhenNonExistingId() {
        UUID id = UUID.randomUUID();
        
        when(pautaRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Pauta> result = pautaService.findById(id);

        assertTrue(result.isEmpty());
        verify(pautaRepository).findById(id);
    }

    @Test
    @DisplayName("Deve buscar todas as pautas cadastradas")
    void shouldReturnListOfPautas() {
        List<Pauta> expectedPautas = Arrays.asList(
            new Pauta("Pauta 1", "Descricao 1"),
            new Pauta("Pauta 2", "Descricao 2")
        );
        
        when(pautaRepository.buscarPautas()).thenReturn(expectedPautas);

        List<Pauta> result = pautaService.buscarPautas();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedPautas, result);
        verify(pautaRepository).buscarPautas();
    }
}
