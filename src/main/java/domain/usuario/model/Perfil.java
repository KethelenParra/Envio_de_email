package domain.usuario.model;

public enum Perfil {

    GERENTE('G'),
    DESENVOLVEDOR('D');

    private final char perfil;

    Perfil(final char perfil) {
        this.perfil = perfil;
    }

    public char getTipo() {
        return this.perfil;
    }

    public static Perfil fromChar(final char perfil) {
        for (final Perfil p : values()) {
            if (p.getTipo() == perfil) {
                return p;
            }
        }
        throw new IllegalArgumentException("Perfil inválido: " + perfil);
    }
}
