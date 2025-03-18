package domain.usuario.model;

import domain.shared.Default;

public class Usuario extends Default {

    private String username;

    private String senha;

    private boolean ativo;

    private Perfil perfil;

    public Usuario() {
    }

    public Usuario(
            final Long id,
            final String username,
            final boolean ativo,
            final Perfil perfil) {
        this.id = id;
        this.username = username;
        this.ativo = ativo;
        this.perfil = perfil;
    }

    public Usuario(final Long id) {
        this.id = id;
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

    public void setSenha(final String senha) {
        this.senha = senha;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(final boolean ativo) {
        this.ativo = ativo;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(final Perfil perfil) {
        this.perfil = perfil;
    }

}
