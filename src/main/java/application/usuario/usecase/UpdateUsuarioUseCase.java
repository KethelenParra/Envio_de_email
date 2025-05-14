package application.usuario.usecase;

import application.auth.service.AuthService;
import infrastructure.auth.dto.AuthUpdateUserDTO;
import application.usuario.service.HashService;
import domain.usuario.model.Perfil;
import domain.usuario.model.Usuario;
import domain.usuario.repository.UsuarioRepository;
import exception.FormValidationException;
import infrastructure.usuario.dto.UsuarioRequestDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import org.keycloak.representations.idm.UserRepresentation;

@ApplicationScoped
public class UpdateUsuarioUseCase {
    private final UsuarioRepository usuarioRepository;
    private final HashService hashService;
    private final AuthService authService;

    @Inject
    public UpdateUsuarioUseCase(final UsuarioRepository usuarioRepository,
            final HashService hashService,
            final AuthService authService) {
        this.usuarioRepository = usuarioRepository;
        this.hashService = hashService;
        this.authService = authService;
    }

    @Transactional
    public UsuarioRequestDTO execute(final Long id,
            final UsuarioRequestDTO dto) {
        Usuario usuario = this.usuarioRepository.findById(id);
        if (usuario == null) {
            throw new FormValidationException("Usuário com ID " + id + " não encontrado.");
        }

        validateInsert(dto);

        Perfil oldPerfil = usuario.getPerfil();

        String keycloakId = usuario.getKeycloakId();
        if (keycloakId == null || keycloakId.isBlank()) {
            List<UserRepresentation> list = authService.findUsersByEmail(usuario.getEmail());
            if (list.isEmpty()) {
                throw new FormValidationException(
                        "Usuário Keycloak não encontrado pelo email: " + usuario.getEmail());
            }
            keycloakId = list.get(0).getId();
            usuario.setKeycloakId(keycloakId);
            usuarioRepository.save(usuario);
        }

        usuario.setUsername(dto.username());
        usuario.setName(dto.name());
        usuario.setCpf(dto.cpf());
        usuario.setEmail(dto.email());
        usuario.setPerfil(Perfil.fromChar(dto.perfil().charAt(0)));
        usuarioRepository.save(usuario);

        AuthUpdateUserDTO kcDto = new AuthUpdateUserDTO(
                dto.email(),
                true, // emailVerified
                true, // enabled
                extractFirstName(dto.name()),
                extractLastName(dto.name()));
        authService.updateUser(keycloakId, kcDto);

        if (!usuario.getPerfil().equals(oldPerfil)) {
            String roleName = usuario.getPerfil() == Perfil.DESENVOLVEDOR
                    ? "DESENVOLVEDOR"
                    : "GERENTE";
            authService.assignRealmRoles(keycloakId, List.of(roleName));
        }

        return dto;
    }

    private void validateInsert(final UsuarioRequestDTO dto) {
        if (dto.perfil().length() != 1) {
            throw new FormValidationException("O perfil informado é inválido.");
        }
        try {
            Perfil.fromChar(dto.perfil().charAt(0));
        } catch (IllegalArgumentException e) {
            throw new FormValidationException("O perfil informado é inválido.");
        }
    }

    private String extractFirstName(String fullName) {
        String[] parts = fullName.trim().split("\\s+");
        return parts.length > 0 ? parts[0] : "";
    }

    private String extractLastName(String fullName) {
        String[] parts = fullName.trim().split("\\s+");
        return parts.length > 1 ? parts[parts.length - 1] : "";
    }
}