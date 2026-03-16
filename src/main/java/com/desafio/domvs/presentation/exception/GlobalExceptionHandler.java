package com.desafio.domvs.presentation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> erros = ex.getBindingResult().getFieldErrors().stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .collect(Collectors.toList());

        ErroResponseDTO body = new ErroResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Dados inválidos na requisição",
                erros
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroResponseDTO> handleBusinessExceptions(IllegalArgumentException ex) {
        ErroResponseDTO body = new ErroResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Violação de Regra de Negócio",
                List.of(ex.getMessage()) 
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}