package com.desafio.domvs.presentation.dto;

import com.desafio.domvs.domain.BeneficioTipo;
import com.desafio.domvs.domain.OfertaTipo;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PropostaRequestDTO {

    @NotBlank(message = "O CPF é obrigatório.")
    @CPF(message = "Formato de CPF inválido.") 
    private String cpf;

    @NotBlank(message = "O nome é obrigatório.")
    private String nome;

    @NotNull(message = "A renda é obrigatória.")
    @PositiveOrZero(message = "A renda não pode ser negativa.")
    private BigDecimal renda;

    @NotNull(message = "O valor dos investimentos é obrigatório.")
    @PositiveOrZero(message = "Os investimentos não podem ser negativos.")
    private BigDecimal investimentos;

    @Min(value = 0, message = "O tempo de conta corrente não pode ser negativo.")
    private int tempoContaCorrenteAnos;

    @NotNull(message = "A oferta deve ser selecionada.")
    private OfertaTipo ofertaSelecionada;

    private List<BeneficioTipo> beneficiosSelecionados;
}