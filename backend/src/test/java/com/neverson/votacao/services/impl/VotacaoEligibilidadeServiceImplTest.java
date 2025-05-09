package com.neverson.votacao.services.impl;


import com.neverson.votacao.config.exceptions.HttpException;
import com.neverson.votacao.domain.dtos.ElegibilidadeVotacaoDTO;
import com.neverson.votacao.domain.enums.EStatusEligibilidade;
import com.neverson.votacao.utils.CPFUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VotacaoEligibilidadeServiceImplTest {

    private VotacaoEligibilidadeServiceImpl service;

    @Mock
    private Random random;

    @BeforeEach
    void setUp() {
        service = new VotacaoEligibilidadeServiceImpl() {
            protected Random createRandom() {
                return random;
            }
        };
    }

    @Test
    @DisplayName("Deve retornar elegibilidade para voto com CPF válido")
    void shouldBeEligibleToVoteWithValidCpf() throws HttpException {
        String validCpf = "12345678901";

        try (MockedStatic<CPFUtils> cpfUtilsMockedStatic = Mockito.mockStatic(CPFUtils.class)) {
            cpfUtilsMockedStatic.when(() -> CPFUtils.isValid(validCpf)).thenReturn(true);
            when(random.nextBoolean()).thenReturn(true);
            ElegibilidadeVotacaoDTO result = service.isEligibleToVote(validCpf);
            assertNotNull(result);
            assertEquals(EStatusEligibilidade.ABLE_TO_VOTE, result.getStatus());
        }
    }

    @Test
    @DisplayName("Deve retornar inelegibilidade para voto com CPF válido")
    void shouldBeUneligibleToVoteWithValidCpf() throws HttpException {
        String validCpf = "12345678901";

        try (MockedStatic<CPFUtils> cpfUtilsMockedStatic = Mockito.mockStatic(CPFUtils.class)) {
            cpfUtilsMockedStatic.when(() -> CPFUtils.isValid(validCpf)).thenReturn(true);

            when(random.nextBoolean()).thenReturn(false);
            ElegibilidadeVotacaoDTO result = service.isEligibleToVote(validCpf);
            assertNotNull(result);
            assertEquals(EStatusEligibilidade.UNABLE_TO_VOTE, result.getStatus());
        }
    }

    @Test
    @DisplayName("Deve lançar exceção para CPF inválido")
    void shouldThrowExceptionForInvalidCpf() {
        String invalidCpf = "08203304566";

        try (MockedStatic<CPFUtils> cpfUtilsMockedStatic = Mockito.mockStatic(CPFUtils.class)) {
            cpfUtilsMockedStatic.when(() -> CPFUtils.isValid(invalidCpf)).thenReturn(false);

            HttpException exception = assertThrows(HttpException.class,
                    () -> service.isEligibleToVote(invalidCpf)
            );

            assertEquals("CPF inválido", exception.getMessage());
        }
    }
}
