package application.usuario.usecase;

import java.util.List;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import application.usuario.mapper.UsuarioMapper;
import application.usuario.service.HashService;
import domain.usuario.model.Perfil;
import domain.usuario.model.Usuario;
import domain.usuario.repository.UsuarioRepository;
import exception.FormValidationException;
import application.auth.usecase.CreateUserUseCase;
import infrastructure.auth.dto.AuthCreateUserDTO;
import infrastructure.usuario.dto.UsuarioRequestDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class CreateUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final HashService hashService;
    private final CreateUserUseCase createUserUseCase;
    private static final String REALM = "MeuRealm";

    @Inject
    public CreateUsuarioUseCase(UsuarioRepository usuarioRepository,
            UsuarioMapper usuarioMapper,
            HashService hashService,
            CreateUserUseCase createUserUseCase) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.hashService = hashService;
        this.createUserUseCase = createUserUseCase;
    }

    @Transactional
    public Long execute(UsuarioRequestDTO dto) {
        validateInsert(dto);

        Usuario u = usuarioMapper.toModel(dto);
        u.setSenha(hashService.getHashSenha(dto.senha()));
        u.setPerfil(Perfil.fromChar(dto.perfil().charAt(0)));
        usuarioRepository.save(u);

        String[] nomes = dto.name().trim().split("\\s+");

        AuthCreateUserDTO authDTO = new AuthCreateUserDTO(
                dto.username(),
                dto.email(),
                true,
                true,
                nomes.length > 0 ? nomes[0] : null,
                nomes.length > 1 ? nomes[nomes.length - 1] : null);
        createUserUseCase.execute(authDTO);

        return u.getId();
    }

    private void validateInsert(final UsuarioRequestDTO usuarioRequestDTO) {

        if (usuarioRequestDTO.perfil().length() != 1) {
            throw new FormValidationException("O perfil informado é inválido.");
        }
        try {
            Perfil.fromChar(usuarioRequestDTO.perfil().charAt(0));
        } catch (IllegalArgumentException e) {
            throw new FormValidationException("O perfil informado é inválido.");
        }
    }
}
