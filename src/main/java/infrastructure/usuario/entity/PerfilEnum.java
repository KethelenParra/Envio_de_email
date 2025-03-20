package infrastructure.usuario.entity;

import domain.usuario.model.Perfil;

public enum PerfilEnum {

    GERENTE("G"),
    DESENVOLVEDOR("D");

    private final String code;

    PerfilEnum(final String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    static PerfilEnum fromCode(final String code) {
        for (final PerfilEnum perfil : values()) {
            if (perfil.getCode().equals(code)) {
                return perfil;
            }
        }
        throw new IllegalArgumentException("Código inválido: " + code);
    }

    public Perfil toDomain() {
        return Perfil.fromChar(this.code.charAt(0));
    }

    public static PerfilEnum fromDomain(final Perfil perfil) {
        return fromCode(String.valueOf(perfil.getTipo()));
    }
}
