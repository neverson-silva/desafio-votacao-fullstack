package com.neverson.votacao.repositories;

import com.neverson.votacao.domain.entities.Sessao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SessaoRepository extends JpaRepository<Sessao, UUID> {
}
