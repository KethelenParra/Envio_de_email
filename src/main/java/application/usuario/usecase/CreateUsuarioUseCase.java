package application.usuario.usecase;

import application.auth.usecase.CreateUserUseCase;

import java.util.List;

import application.auth.service.AuthService;
import infrastructure.auth.dto.AuthCreateUserDTO;
import infrastructure.auth.dto.AuthResetPasswordUserDTO;
import infrastructure.usuario.dto.UsuarioRequestDTO;
import application.usuario.mapper.UsuarioMapper;
import application.usuario.service.HashService;
import domain.usuario.model.Perfil;
import domain.usuario.model.Usuario;
import domain.usuario.repository.UsuarioRepository;
import exception.FormValidationException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CreateUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final HashService hashService;
    private final CreateUserUseCase createUserUseCase;
    private final AuthService authService;
    private static final String REALM = "MeuRealm";

    @Inject
    public CreateUsuarioUseCase(UsuarioRepository usuarioRepository,
            UsuarioMapper usuarioMapper,
            HashService hashService,
            CreateUserUseCase createUserUseCase,
            AuthService authService) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.hashService = hashService;
        this.createUserUseCase = createUserUseCase;
        this.authService = authService;
    }

    @Transactional
    public Long execute(UsuarioRequestDTO dto) {
        validateInsert(dto);

        // 1) Salvando no banco com CPF como senha
        Usuario u = usuarioMapper.toModel(dto);
        u.setSenha(hashService.getHashSenha(dto.cpf()));
        u.setPerfil(Perfil.fromChar(dto.perfil().charAt(0)));
        usuarioRepository.save(u);

        // 2) Criar usuário no Keycloak e obter o ID
        AuthCreateUserDTO authDTO = new AuthCreateUserDTO(
                dto.username(),
                dto.email(),
                true,
                true,
                getFirstName(dto.name()),
                getLastName(dto.name()));
        String keycloakId = createUserUseCase.execute(authDTO);

        // 3) Resetar a senha no Keycloak para ser o próprio CPF
        AuthResetPasswordUserDTO pwdDto = new AuthResetPasswordUserDTO(
                "password",
                dto.cpf(),
                false);
        authService.resetPassword(keycloakId, pwdDto);

        String roleName = dto.perfil().charAt(0) == 'D'
                ? "DESENVOLVEDOR"
                : "GERENTE";
        authService.assignRealmRoles(keycloakId, List.of(roleName));

        return u.getId();
    }

    private void validateInsert(UsuarioRequestDTO dto) {
        if (usuarioRepository.findByEmail(dto.email()) != null) {
            throw new FormValidationException("E-mail já cadastrado: " + dto.email());
        }
        if (usuarioRepository.findByCpf(dto.cpf()) != null) {
            throw new FormValidationException("CPF já cadastrado: " + dto.cpf());
        }
        if (dto.perfil().length() != 1) {
            throw new FormValidationException("O perfil informado é inválido.");
        }
        try {
            Perfil.fromChar(dto.perfil().charAt(0));
        } catch (IllegalArgumentException e) {
            throw new FormValidationException("O perfil informado é inválido.");
        }
    }

    private String getFirstName(String name) {
        String[] parts = name.trim().split("\\s+");
        return parts.length > 0 ? parts[0] : "";
    }

    private String getLastName(String name) {
        String[] parts = name.trim().split("\\s+");
        return parts.length > 1 ? parts[parts.length - 1] : "";
    }
}
