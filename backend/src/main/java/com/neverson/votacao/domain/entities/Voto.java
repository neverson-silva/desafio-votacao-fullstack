package com.neverson.votacao.domain.entities;

import com.neverson.votacao.domain.enums.EVotoOpcao;
import com.neverson.votacao.config.database.EVotoOpcaoConverter;
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
public class Voto {
    @Id
    private UUID id;

    // por simplicidade do desafio sera o cpf
    @Column(nullable = false, length = 11)
    private String associadoId;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false, columnDefinition = "TINYINT")
    @Convert(converter = EVotoOpcaoConverter.class)
    private EVotoOpcao voto;

    @ManyToOne
    private Pauta pauta;

    public Voto(String associadoId, EVotoOpcao voto, Pauta pauta) {
        this.associadoId = associadoId;
        this.voto = voto;
        this.pauta = pauta;
    }

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUIDUtils.generate();
        }
    }
}
