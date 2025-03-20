package application.usuario.usecase;

import java.util.List;

import domain.usuario.model.Usuario;
import domain.usuario.repository.UsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GetAllUsuarioUseCase {
    private final UsuarioRepository usuarioRepository;

    public GetAllUsuarioUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> execute() {
        return usuarioRepository.findAll();
    }
}
