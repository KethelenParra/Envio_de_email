package infrastructure.usuario.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PerfilEnumConverter implements AttributeConverter<PerfilEnum, String> {

    @Override
    public String convertToDatabaseColumn(final PerfilEnum perfilEnum) {
        return perfilEnum != null ? perfilEnum.getCode() : null;
    }

    @Override
    public PerfilEnum convertToEntityAttribute(final String code) {
        return code != null ? PerfilEnum.fromCode(code) : null;
    }

}
