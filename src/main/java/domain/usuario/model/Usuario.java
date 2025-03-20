package domain.usuario.model;

import domain.shared.Default;

public class Usuario extends Default {

    private String username;
    private String name;
    private String cpf;
    private String email;
    private String senha;
    private Perfil perfil;

    public Usuario() {
    }

    public Usuario(final String username, final String name, final String cpf, final String email,
            final Perfil perfil) {
        this.username = username;
        this.name = name;
        this.cpf = cpf;
        this.email = email;
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

    public void setSenha(final String senha) {
        this.senha = senha;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(final Perfil perfil) {
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
}
