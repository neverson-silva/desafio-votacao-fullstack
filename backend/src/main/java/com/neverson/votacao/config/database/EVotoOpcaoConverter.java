package com.neverson.votacao.config.database;

import com.neverson.votacao.domain.enums.EVotoOpcao;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class EVotoOpcaoConverter implements AttributeConverter<EVotoOpcao, Integer> {

    @Override
    public Integer convertToDatabaseColumn(EVotoOpcao atributo) {
        return atributo == null ? null : atributo.ordinal();
    }

    @Override
    public EVotoOpcao convertToEntityAttribute(Integer dbData) {
        if (dbData == null) return null;
        return EVotoOpcao.values()[dbData];
    }
}
