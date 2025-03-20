package infrastructure.usuario.dto;

public class UsuarioResponseDTO {

    private Long id;
    private String username;
    private String name;
    private String cpf;
    private String email;
    private char perfil;

    public UsuarioResponseDTO() {
    }

    public UsuarioResponseDTO(
            final Long id,
            final String username,
            final String name,
            final String cpf,
            final String email,
            final char perfil) {

        this.id = id;
        this.username = username;
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.perfil = perfil;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public char getPerfil() {
        return perfil;
    }

    public void setPerfil(char perfil) {
        this.perfil = perfil;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
