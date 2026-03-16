package com.desafio.domvs.domain.service;

import com.desafio.domvs.domain.OfertaTipo;
import com.desafio.domvs.domain.Proposta;

public interface RegraElegibilidade {
    OfertaTipo getOfertaSuportada();
    boolean isElegivel(Proposta proposta);
}