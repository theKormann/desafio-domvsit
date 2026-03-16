package com.desafio.domvs.presentation.dto;

import com.desafio.domvs.domain.BeneficioTipo;
import com.desafio.domvs.domain.OfertaTipo;
import com.desafio.domvs.domain.StatusProposta;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class PropostaResponseDTO {
    private UUID id;
    private OfertaTipo ofertaSelecionada;
    private List<BeneficioTipo> beneficiosAtivos;
    private StatusProposta status;
}