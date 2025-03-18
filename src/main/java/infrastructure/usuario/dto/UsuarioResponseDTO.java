package infrastructure.usuario.dto;

public class UsuarioResponseDTO {

    private Long id;

    private boolean isAtivo;

    private char perfil;

    public UsuarioResponseDTO() {
    }

    public UsuarioResponseDTO(
            final Long id,
            final boolean isAtivo,
            final char perfil) {

        this.id = id;
        this.isAtivo = isAtivo;
        this.perfil = perfil;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isAtivo() {
        return isAtivo;
    }

    public void setAtivo(boolean isAtivo) {
        this.isAtivo = isAtivo;
    }

    public char getPerfil() {
        return perfil;
    }

    public void setPerfil(char perfil) {
        this.perfil = perfil;
    }

}
