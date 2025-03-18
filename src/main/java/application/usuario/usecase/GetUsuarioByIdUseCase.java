package application.usuario.usecase;

import domain.usuario.model.Usuario;
import domain.usuario.repository.UsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GetUsuarioByIdUseCase {

    private final UsuarioRepository usuarioRepository;

    public GetUsuarioByIdUseCase(final UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario execute(final Long id) {
        return this.usuarioRepository.findById(id);
    }
}
