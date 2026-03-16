package com.desafio.domvs.domain.repository;

import com.desafio.domvs.domain.ContaCartao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContaCartaoRepository extends JpaRepository<ContaCartao, UUID> {
}