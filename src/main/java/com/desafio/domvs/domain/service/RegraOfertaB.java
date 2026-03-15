package com.desafio.domvs.domain.service;

import com.desafio.domvs.domain.OfertaTipo;
import com.desafio.domvs.domain.Proposta;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class RegraOfertaB {

    @Override
    public OfertaTipo getOfertaSuportada() {
        return OfertaTipo.OFERTA_B;
    }

    @Override
    public boolean isElegivel(Proposta proposta) {
        if (proposta.getRenda() == null || proposta.getInvestimentos() == null) return false;
        
        boolean rendaElegivel = proposta.getRenda().compareTo(new BigDecimal("15000.00")) > 0;
        boolean investimentosElegiveis = proposta.getInvestimentos().compareTo(new BigDecimal("5000.00")) > 0;
        
        return rendaElegivel && investimentosElegiveis;
    }
}