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
    private final Keycloak keycloakAdmin;

    private static final String REALM = "MeuRealm";

    @Inject
    public CreateUsuarioUseCase(final UsuarioRepository usuarioRepository, final UsuarioMapper usuarioMapper,
            final HashService hashService, final Keycloak keycloakAdmin) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.hashService = hashService;
        this.keycloakAdmin = keycloakAdmin;
    }

    @Transactional
    public Long execute(UsuarioRequestDTO dto) {
        validateInsert(dto);

        // 1) Persiste no banco
        Usuario u = usuarioMapper.toModel(dto);
        u.setSenha(hashService.getHashSenha(dto.senha()));
        u.setPerfil(Perfil.fromChar(dto.perfil().charAt(0)));
        usuarioRepository.save(u);

        // 2) Cria usuário no Keycloak
        UserRepresentation rep = new UserRepresentation();
        rep.setUsername(dto.username());
        rep.setEmail(dto.email());
        rep.setEmailVerified(true);
        rep.setEnabled(true);
        String[] nomes = dto.name().trim().split("\\s+");
        if (nomes.length > 0) {
            rep.setFirstName(nomes[0]); // primeiro token
            if (nomes.length > 1) {
                rep.setLastName(nomes[nomes.length - 1]); // último token
            }
        }

        Response resp = keycloakAdmin.realm(REALM).users().create(rep);
        if (resp.getStatus() != 201) {
            throw new RuntimeException("Keycloak error: HTTP "
                    + resp.getStatus() + " → " + resp.readEntity(String.class));
        }
        String kcId = resp.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

        // 3) Define a senha no Keycloak
        CredentialRepresentation cred = new CredentialRepresentation();
        cred.setType(CredentialRepresentation.PASSWORD);
        cred.setValue(dto.senha());
        cred.setTemporary(false);
        keycloakAdmin.realm(REALM).users().get(kcId).resetPassword(cred);

        // 4) Atribui o papel de acordo com o perfil
        String roleName = (u.getPerfil() == Perfil.GERENTE) ? "GERENTE" : "DESENVOLVEDOR";
        var roleRep = keycloakAdmin
                .realm(REALM)
                .roles()
                .get(roleName)
                .toRepresentation();

        keycloakAdmin
                .realm(REALM)
                .users()
                .get(kcId)
                .roles()
                .realmLevel()
                .add(List.of(roleRep));

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
