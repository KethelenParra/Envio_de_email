package application.usuario.usecase;

import jakarta.enterprise.context.ApplicationScoped;
import domain.usuario.repository.UsuarioRepository;
import domain.usuario.model.Usuario;
import exception.FormValidationException;

@ApplicationScoped
public class GetUsuarioByEmailUseCase {
    private final UsuarioRepository usuarioRepository;

    public GetUsuarioByEmailUseCase(final UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario execute(final String email) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            throw new FormValidationException("Usuário não encontrado.");
        }
        return usuario;
    }
}
