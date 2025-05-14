package infrastructure.usuario.entity;

import infrastructure.shared.DefaultEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;

@Entity(name = "Usuario")
public class UsuarioEntity extends DefaultEntity {

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "cpf", nullable = false)
    private String cpf;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "senha")
    private String senha;

    @Convert(converter = PerfilEnumConverter.class)
    @Column(name = "perfil", nullable = false)
    private PerfilEnum perfil;

    @Column(name = "keycloak_id", unique = true, nullable = true)
    private String keycloakId;

    public UsuarioEntity() {
    }

    public UsuarioEntity(
            final String username,
            final String senha,
            final String name,
            final String cpf,
            final String email,
            final PerfilEnum perfil,
            final String keycloakId) {

        this.username = username;
        this.senha = senha;
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.perfil = perfil;
        this.keycloakId = keycloakId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public PerfilEnum getPerfil() {
        return perfil;
    }

    public void setPerfil(final PerfilEnum perfil) {
        this.perfil = perfil;
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

    public String getKeycloakId() {
        return keycloakId;
    }

    public void setKeycloakId(String keycloakId) {
        this.keycloakId = keycloakId;
    }
}
