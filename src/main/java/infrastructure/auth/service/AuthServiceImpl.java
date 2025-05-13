package infrastructure.auth.service;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;

import application.auth.service.AuthService;
import infrastructure.auth.dto.AuthCreateUserDTO;
import infrastructure.auth.dto.AuthLoginDTO;
import infrastructure.auth.dto.AuthLogoutDTO;
import infrastructure.auth.dto.AuthResetPasswordUserDTO;
import infrastructure.auth.dto.AuthUpdateUserDTO;
import infrastructure.auth.dto.TokenResponseDTO;
import infrastructure.auth.service.client.KeycloakAuthClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

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
