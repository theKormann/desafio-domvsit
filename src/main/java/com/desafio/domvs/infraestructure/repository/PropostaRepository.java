package com.desafio.domvs.domain.repository;

import com.desafio.domvs.domain.Proposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PropostaRepository extends JpaRepository<Proposta, UUID> {
}