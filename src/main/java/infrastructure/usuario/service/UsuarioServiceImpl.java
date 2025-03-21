package infrastructure.usuario.service;

import java.util.List;
import application.usuario.service.UsuarioService;
import application.usuario.usecase.AutenticarUsuarioUseCase;
import application.usuario.usecase.CreateUsuarioUseCase;
import application.usuario.usecase.DeleteUsuarioUseCase;
import application.usuario.usecase.GetAllUsuarioUseCase;
import application.usuario.usecase.GetUsuarioByIdUseCase;
import application.usuario.usecase.ResetPasswordUseCase;
import application.usuario.usecase.UpdateUsuarioUseCase;
import domain.usuario.model.Usuario;
import infrastructure.usuario.dto.ResetPasswordResponseDTO;
import infrastructure.usuario.dto.UsuarioRequestDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UsuarioServiceImpl implements UsuarioService {

    private final GetUsuarioByIdUseCase getUsuarioByIdUseCase;
    private final CreateUsuarioUseCase createUsuarioUseCase;
    private final UpdateUsuarioUseCase updateUsuarioUseCase;
    private final DeleteUsuarioUseCase deleteUsuarioUseCase;
    private final GetAllUsuarioUseCase getAllUsuarioUseCase;
    private final AutenticarUsuarioUseCase autenticarUsuarioUseCase;
    private final ResetPasswordUseCase resetPasswordUseCase;

    @Inject
    public UsuarioServiceImpl(
            final GetUsuarioByIdUseCase getUsuarioByIdUseCase,
            final CreateUsuarioUseCase createUsuarioUseCase,
            final UpdateUsuarioUseCase updateUsuarioUseCase,
            final DeleteUsuarioUseCase deleteUsuarioUseCase,
            final GetAllUsuarioUseCase getAllUsuarioUseCase,
            final AutenticarUsuarioUseCase autenticarUsuarioUseCase,
            final ResetPasswordUseCase resetPasswordUseCase) {

        this.getUsuarioByIdUseCase = getUsuarioByIdUseCase;
        this.createUsuarioUseCase = createUsuarioUseCase;
        this.updateUsuarioUseCase = updateUsuarioUseCase;
        this.deleteUsuarioUseCase = deleteUsuarioUseCase;
        this.getAllUsuarioUseCase = getAllUsuarioUseCase;
        this.autenticarUsuarioUseCase = autenticarUsuarioUseCase;
        this.resetPasswordUseCase = resetPasswordUseCase;
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

    @Override
    public List<Usuario> getAllUsuarios() {
        return this.getAllUsuarioUseCase.execute();
    }

    @Override
    public Usuario findByUsernameAndSenha(String username, String senha) {
        return this.autenticarUsuarioUseCase.execute(username, senha);
    }

    @Override
    public Usuario findByUsername(String username) {
        return this.autenticarUsuarioUseCase.executeUsername(username);
    }

    @Override
    public void alterarSenha(Long userId, ResetPasswordResponseDTO resetPasswordResponseDTO) {
        this.resetPasswordUseCase.alterarSenha(userId, resetPasswordResponseDTO);
    }

}
