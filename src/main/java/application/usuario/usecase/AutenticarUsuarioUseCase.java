package application.usuario.usecase;

import domain.usuario.model.Usuario;
import domain.usuario.repository.UsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AutenticarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    public AutenticarUsuarioUseCase(final UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario execute(final String username, final String senha) {
        return this.usuarioRepository.findByUsernameAndSenha(username, senha);
    }

    public Usuario executeUsername(final String username) {
        return this.usuarioRepository.findByUsername(username);
    }
}
