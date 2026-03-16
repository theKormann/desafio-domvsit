package com.desafio.domvs.service;

import com.desafio.domvs.domain.OfertaTipo;
import com.desafio.domvs.domain.Proposta;
import com.desafio.domvs.domain.service.RegraElegibilidade;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class RegraOfertaA implements RegraElegibilidade {
    
    @Override
    public OfertaTipo getOfertaSuportada() {
        return OfertaTipo.OFERTA_A;
    }

    @Override
    public boolean isElegivel(Proposta proposta) {
        if (proposta.getRenda() == null) return false;
        return proposta.getRenda().compareTo(new BigDecimal("1000.00")) > 0;
    }
}