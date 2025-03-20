package application.usuario.usecase;

import domain.usuario.repository.UsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class DeleteUsuarioUseCase {

    private final domain.usuario.repository.UsuarioRepository usuarioRepository;

    @Inject
    public DeleteUsuarioUseCase(final UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public void execute(final Long id) {
        this.usuarioRepository.deleteById(id);
    }
}