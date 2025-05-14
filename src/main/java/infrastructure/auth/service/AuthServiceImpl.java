package infrastructure.auth.service;

import application.auth.service.AuthService;
import infrastructure.auth.dto.*;
import infrastructure.auth.service.client.KeycloakAuthClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class AuthServiceImpl implements AuthService {

    @Inject
    @RestClient
    KeycloakAuthClient keycloakAuthClient;

    @ConfigProperty(name = "keycloak.realm")
    String realm;

    @ConfigProperty(name = "keycloak.client-id")
    String clientId;

    @ConfigProperty(name = "keycloak.client-secret")
    String clientSecret;

    @ConfigProperty(name = "keycloak.server-url")
    String keycloakServerUrl;

    @Override
    public String createUser(AuthCreateUserDTO dto) {
        String bearer = "Bearer " + getAdminAccessToken();
        Response resp = keycloakAuthClient.createUser(bearer, realm, dto);
        if (resp.getStatus() != 201) {
            throw new RuntimeException("Erro ao criar usuário no Keycloak: "
                    + resp.readEntity(String.class));
        }
        // extrai ID da URL: /admin/realms/MeuRealm/users/{id}
        String location = resp.getHeaderString("Location");
        return location.substring(location.lastIndexOf('/') + 1);
    }

    @Override
    public void updateUser(String id, AuthUpdateUserDTO dto) {
        String bearer = "Bearer " + getAdminAccessToken();
        keycloakAuthClient.updateUser(bearer, realm, id, dto);
    }

    @Override
    public void deleteUser(String id) {
        String bearer = "Bearer " + getAdminAccessToken();
        keycloakAuthClient.deleteUser(bearer, realm, id);
    }

    @Override
    public void resetPassword(String id, AuthResetPasswordUserDTO dto) {
        String bearer = "Bearer " + getAdminAccessToken();
        keycloakAuthClient.resetPassword(bearer, realm, id, dto);
    }

    @Override
    public TokenResponseDTO login(AuthLoginDTO dto) {
        return keycloakAuthClient.login(
                realm,
                "password",
                dto.username(),
                dto.senha(),
                clientId,
                clientSecret);
    }

    @Override
    public void logout(AuthLogoutDTO dto) {
        keycloakAuthClient.logout(
                realm,
                dto.refreshToken(),
                clientId,
                clientSecret);
    }

    @Override
    public void assignRealmRoles(String userId, List<String> rolesNames) {
        String bearer = "Bearer " + getAdminAccessToken();
        // 1) Obter RoleRepresentation de cada nome
        List<RoleRepresentation> reps = rolesNames.stream()
                .map(role -> keycloakAuthClient.getRealmRole(bearer, realm, role))
                .collect(Collectors.toList());

        // 2) Atribuir ao usuário
        Response resp = keycloakAuthClient.addRealmRoleMapping(bearer, realm, userId, reps);
        if (resp.getStatus() != 204) {
            throw new RuntimeException("Erro ao atribuir papéis no Keycloak: " + resp.readEntity(String.class));
        }
    }

    @Override
    public String findKeycloakIdByUsername(String username) {
        String bearer = "Bearer " + getAdminAccessToken();

        List<UserRepresentation> users = keycloakAuthClient.searchUser(bearer, realm, username);
        if (users == null || users.isEmpty()) {
            throw new RuntimeException("Usuário Keycloak não encontrado: " + username);
        }
        return users.get(0).getId();
    }

    @Override
    public List<UserRepresentation> findUsersByEmail(String email) {
        String bearer = "Bearer " + getAdminAccessToken();
        return keycloakAuthClient.searchUser(bearer, realm, email);
    }

    public String getAdminAccessToken() {
        TokenResponseDTO tok = keycloakAuthClient.login(
                realm,
                "client_credentials",
                null,
                null,
                clientId,
                clientSecret);
        return tok.accessToken();
    }
}
