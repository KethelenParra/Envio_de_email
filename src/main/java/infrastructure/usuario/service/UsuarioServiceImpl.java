package br.tc.tceto.gta.gtawebservice.infrastructure.usuario.service;

import java.util.Map;

import br.tc.tceto.gta.gtawebservice.application.usuario.service.UsuarioService;
import br.tc.tceto.gta.gtawebservice.application.usuario.usecase.AutenticarUsuarioUseCase;
import br.tc.tceto.gta.gtawebservice.application.usuario.usecase.CreateUsuarioUseCase;
import br.tc.tceto.gta.gtawebservice.application.usuario.usecase.DeleteUsuarioUseCase;
import br.tc.tceto.gta.gtawebservice.application.usuario.usecase.GetAllUsuarioUseCase;
import br.tc.tceto.gta.gtawebservice.application.usuario.usecase.GetUsuarioByIdUseCase;
import br.tc.tceto.gta.gtawebservice.application.usuario.usecase.UpdateUsuarioUseCase;
import br.tc.tceto.gta.gtawebservice.domain.servidor.model.Servidor;
import br.tc.tceto.gta.gtawebservice.domain.usuario.model.Usuario;
import br.tc.tceto.gta.gtawebservice.infrastructure.usuario.dto.UsuarioRequestDTO;
import br.tc.tceto.gta.gtawebservice.infrastructure.usuario.dto.UsuarioResponseDTO;
import br.tc.tceto.gta.gtawebservice.util.PagedResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UsuarioServiceImpl implements UsuarioService {

    private final GetAllUsuarioUseCase getAllUsuarioUseCase;

    private final GetUsuarioByIdUseCase getUsuarioByIdUseCase;

    private final CreateUsuarioUseCase createUsuarioUseCase;

    private final UpdateUsuarioUseCase updateUsuarioUseCase;

    private final DeleteUsuarioUseCase deleteUsuarioUseCase;

    private final AutenticarUsuarioUseCase autenticarUsuarioUseCase;

    @Inject
    public UsuarioServiceImpl(
            final GetAllUsuarioUseCase getAllUsuarioUseCase,
            final GetUsuarioByIdUseCase getUsuarioByIdUseCase,
            final CreateUsuarioUseCase createUsuarioUseCase,
            final UpdateUsuarioUseCase updateUsuarioUseCase,
            final DeleteUsuarioUseCase deleteUsuarioUseCase,
            final AutenticarUsuarioUseCase autenticarUsuarioUseCase) {

        this.getAllUsuarioUseCase = getAllUsuarioUseCase;
        this.getUsuarioByIdUseCase = getUsuarioByIdUseCase;
        this.createUsuarioUseCase = createUsuarioUseCase;
        this.updateUsuarioUseCase = updateUsuarioUseCase;
        this.deleteUsuarioUseCase = deleteUsuarioUseCase;
        this.autenticarUsuarioUseCase = autenticarUsuarioUseCase;
    }

    @Override
    public PagedResponseDTO<UsuarioResponseDTO> getAllUsers(Map<String, Object> filtro) {
        return this.getAllUsuarioUseCase.execute(filtro);
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
    public Usuario findByUsernameAndSenha(String username, String senha) {
        return this.autenticarUsuarioUseCase.execute(username, senha);
    }

    @Override
    public Usuario findByUsername(String username) {
        return this.autenticarUsuarioUseCase.executeUsername(username);
    }

    public Boolean validateCpf(String cpf) {
        Servidor servidor = this.createUsuarioUseCase.validateCpf(cpf);
        return servidor != null;
    }
}
