package com.neverson.votacao.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.neverson.votacao.utils.UUIDUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sessao {
    @Id
    private UUID id;

    @OneToOne
    @JsonIgnore
    private Pauta pauta;

    private LocalDateTime abertura;

    private LocalDateTime fechamento;

    public Sessao(Pauta pauta, LocalDateTime abertura, LocalDateTime fechamento) {
        this.pauta = pauta;
        this.abertura = abertura;
        this.fechamento = fechamento;
    }

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUIDUtils.generate();
        }
    }
}
