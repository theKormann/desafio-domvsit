package com.desafio.domvs.presentation;

import com.desafio.domvs.application.PropostaService;
import com.desafio.domvs.domain.Proposta;
import com.desafio.domvs.presentation.dto.PropostaRequestDTO;
import com.desafio.domvs.presentation.dto.PropostaResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/propostas")
public class PropostaController {

    private final PropostaService propostaService;

    public PropostaController(PropostaService propostaService) {
        this.propostaService = propostaService;
    }

    @PostMapping
    public ResponseEntity<PropostaResponseDTO> submeterProposta(@Valid @RequestBody PropostaRequestDTO dto) {
        
        Proposta proposta = new Proposta();
        proposta.setCpf(dto.getCpf());
        proposta.setNome(dto.getNome());
        proposta.setRenda(dto.getRenda());
        proposta.setInvestimentos(dto.getInvestimentos());
        proposta.setTempoContaCorrenteAnos(dto.getTempoContaCorrenteAnos());
        proposta.setOfertaSelecionada(dto.getOfertaSelecionada());
        proposta.setBeneficiosSelecionados(dto.getBeneficiosSelecionados());

        Proposta propostaProcessada = propostaService.processarNovaProposta(proposta);

        PropostaResponseDTO responseDTO = new PropostaResponseDTO();
        responseDTO.setId(propostaProcessada.getId());
        responseDTO.setOfertaSelecionada(propostaProcessada.getOfertaSelecionada());
        responseDTO.setBeneficiosAtivos(propostaProcessada.getBeneficiosSelecionados());
        responseDTO.setStatus(propostaProcessada.getStatus());

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
}