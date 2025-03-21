package application.usuario.service;

public interface HashService {
    String getHashSenha(String senha);

    boolean verificandoHash(String senhaDigitada, String hashArmazenado);
}
