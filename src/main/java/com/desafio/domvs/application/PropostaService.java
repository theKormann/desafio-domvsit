package com.desafio.domvs.application;

import com.desafio.domvs.domain.BeneficioTipo;
import com.desafio.domvs.domain.OfertaTipo;
import com.desafio.domvs.domain.Proposta;
import com.desafio.domvs.domain.StatusProposta;
import com.desafio.domvs.domain.service.RegraElegibilidade;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropostaService {

    private final List<RegraElegibilidade> regrasElegibilidade;

    public PropostaService(List<RegraElegibilidade> regrasElegibilidade) {
        this.regrasElegibilidade = regrasElegibilidade;
    }

    public Proposta processarNovaProposta(Proposta proposta) {
        proposta.setStatus(StatusProposta.EM_ANALISE);

        validarBeneficios(proposta);

        boolean aprovado = regrasElegibilidade.stream()
                .filter(regra -> regra.getOfertaSuportada() == proposta.getOfertaSelecionada())
                .findFirst()
                .map(regra -> regra.isElegivel(proposta))
                .orElse(false);
        if (aprovado) {
            proposta.setStatus(StatusProposta.APROVADA);
        } else {
            proposta.setStatus(StatusProposta.REJEITADA);
        }

        return proposta;
    }

    private void validarBeneficios(Proposta proposta) {
        List<BeneficioTipo> beneficios = proposta.getBeneficiosSelecionados();
        if (beneficios == null || beneficios.isEmpty()) {
            return;
        } else if (beneficios.contains(BeneficioTipo.CASHBACK) && beneficios.contains(BeneficioTipo.PONTOS)) {
            throw new IllegalArgumentException("Não é possível selecionar Cashback e Pontos simultaneamente.");
        }

        if (beneficios.contains(BeneficioTipo.SEGURO_VIAGEM)
                && proposta.getOfertaSelecionada() != OfertaTipo.OFERTA_C) {
            throw new IllegalArgumentException("O seguro Viagem está disponível apenas para a oferta tipo C");
        }

        if (beneficios.contains(BeneficioTipo.SALA_VIP) && proposta.getOfertaSelecionada() == OfertaTipo.OFERTA_A) {
            throw new IllegalArgumentException("A sala VIP está disponível apenas para as ofertas tipo B e C.");
        }
    }

}
