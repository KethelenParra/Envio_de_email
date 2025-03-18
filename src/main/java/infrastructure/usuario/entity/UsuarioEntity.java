package infrastructure.usuario.entity;

import infrastructure.shared.DefaultEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;

@Entity(name = "Usuario")
public class UsuarioEntity extends DefaultEntity {

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "senha")
    private String senha;

    @Column(name = "ativo", nullable = false)
    private boolean ativo;

    @Convert(converter = PerfilEnumConverter.class)
    @Column(name = "perfil", nullable = false)
    private PerfilEnum perfil;

    public UsuarioEntity() {
    }

    public UsuarioEntity(
            final String username,
            final String senha,
            final boolean ativo,
            final PerfilEnum perfil) {

        this.username = username;
        this.senha = senha;
        this.ativo = ativo;
        this.perfil = perfil;
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

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(final boolean ativo) {
        this.ativo = ativo;
    }

    public PerfilEnum getPerfil() {
        return perfil;
    }

    public void setPerfil(final PerfilEnum perfil) {
        this.perfil = perfil;
    }

}
