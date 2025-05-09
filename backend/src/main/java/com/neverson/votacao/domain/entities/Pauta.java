package com.neverson.votacao.domain.entities;

import com.neverson.votacao.utils.UUIDUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pauta {
    @Id
    private UUID id;
    private String titulo;
    private String descricao;

    @OneToOne(mappedBy = "pauta", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    private Sessao sessao;

    public Pauta(String titulo, String descricao) {
        this.titulo = titulo;
        this.descricao = descricao;
    }

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUIDUtils.generate();
        }
    }
}
