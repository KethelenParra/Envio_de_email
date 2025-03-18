package infrastructure.usuario.service;

import application.usuario.service.UsuarioService;
import application.usuario.usecase.CreateUsuarioUseCase;
import application.usuario.usecase.DeleteUsuarioUseCase;
import application.usuario.usecase.GetUsuarioByIdUseCase;
import application.usuario.usecase.UpdateUsuarioUseCase;
import domain.usuario.model.Usuario;
import infrastructure.usuario.dto.UsuarioRequestDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UsuarioServiceImpl implements UsuarioService {

    private final GetUsuarioByIdUseCase getUsuarioByIdUseCase;

    private final CreateUsuarioUseCase createUsuarioUseCase;

    private final UpdateUsuarioUseCase updateUsuarioUseCase;

    private final DeleteUsuarioUseCase deleteUsuarioUseCase;

    @Inject
    public UsuarioServiceImpl(
            final GetUsuarioByIdUseCase getUsuarioByIdUseCase,
            final CreateUsuarioUseCase createUsuarioUseCase,
            final UpdateUsuarioUseCase updateUsuarioUseCase,
            final DeleteUsuarioUseCase deleteUsuarioUseCase) {

        this.getUsuarioByIdUseCase = getUsuarioByIdUseCase;
        this.createUsuarioUseCase = createUsuarioUseCase;
        this.updateUsuarioUseCase = updateUsuarioUseCase;
        this.deleteUsuarioUseCase = deleteUsuarioUseCase;
    }

    @Override
    public Usuario getUsuarioById(Long id) {
        return this.getUsuarioByIdUseCase.execute(id);
    }

    @Override
    public Long createUsuario(UsuarioRequestDTO usuarioRequestDTO) {
        return this.createUsuarioUseCase.execute(usuarioRequestDTO);
    }

    @Override
    public UsuarioRequestDTO updateUsuario(Long id, UsuarioRequestDTO usuarioRequestDTO) {
        return this.updateUsuarioUseCase.execute(id, usuarioRequestDTO);
    }

    @Override
    public void deleteUsuario(Long id) {
        this.deleteUsuarioUseCase.execute(id);
    }
}
