package application.usuario.usecase;

import domain.usuario.model.Usuario;
import domain.usuario.repository.UsuarioRepository;
import exception.FormValidationException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GetUsuarioByCpfUseCase {
    private final UsuarioRepository usuarioRepository;

    public GetUsuarioByCpfUseCase(final UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario execute(final String cpf) {
        Usuario usuario = usuarioRepository.findByCpf(cpf);
        if (usuario == null) {
            throw new FormValidationException("Usuário não encontrado.");
        }
        return usuario;
    }
}
