package com.desafio.domvs.presentation.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErroResponseDTO {
    private LocalDateTime timestamp;
    private Integer status;
    private String erro;
    private List<String> detalhes;
}