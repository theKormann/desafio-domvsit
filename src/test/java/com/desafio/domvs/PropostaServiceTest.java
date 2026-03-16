package com.desafio.domvs.application;

import com.desafio.domvs.domain.BeneficioTipo;
import com.desafio.domvs.domain.OfertaTipo;
import com.desafio.domvs.domain.Proposta;
import com.desafio.domvs.domain.repository.PropostaRepository;
import com.desafio.domvs.domain.service.RegraElegibilidade;
import com.desafio.domvs.infrastructure.messaging.PropostaPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class PropostaServiceTest {

    @Mock
    private List<RegraElegibilidade> regrasElegibilidade;

    @Mock
    private PropostaRepository propostaRepository;

    @Mock
    private PropostaPublisher propostaPublisher;

    @InjectMocks
    private PropostaService propostaService;

    private Proposta proposta;

    @BeforeEach
    void setUp() {
        proposta = new Proposta();
    }

    @Test
    void deveLancarExcecaoQuandoCashbackEPontosForemSelecionadosJuntos() {
        proposta.setBeneficiosSelecionados(List.of(BeneficioTipo.CASHBACK, BeneficioTipo.PONTOS));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            propostaService.processarNovaProposta(proposta);
        });

        assertEquals("Não é possível selecionar Cashback e Pontos simultaneamente.", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoSeguroViagemForSelecionadoForaDaOfertaC() {
        proposta.setOfertaSelecionada(OfertaTipo.OFERTA_A);
        proposta.setBeneficiosSelecionados(List.of(BeneficioTipo.SEGURO_VIAGEM));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            propostaService.processarNovaProposta(proposta);
        });

        assertEquals("O seguro Viagem está disponível apenas para a oferta tipo C", exception.getMessage());
    }
}