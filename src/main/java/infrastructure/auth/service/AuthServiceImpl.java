package infrastructure.auth.service;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

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

    @Override
    public void createUser(AuthCreateUserDTO dto) {
        var response = keycloakAuthClient.createUser(realm, dto);
        if (response.getStatus() != 201) {
            throw new RuntimeException("Erro ao criar usuário: " + response.readEntity(String.class));
        }
    }

    @Override
    public void updateUser(String id, AuthUpdateUserDTO dto) {
        keycloakAuthClient.updateUser(realm, id, dto);
    }

    @Override
    public void deleteUser(String id) {
        keycloakAuthClient.deleteUser(realm, id);
    }

    @Override
    public void resetPassword(String id, AuthResetPasswordUserDTO dto) {
        keycloakAuthClient.resetPassword(realm, id, dto);
    }

    @Override
    public TokenResponseDTO login(AuthLoginDTO dto) {
        return keycloakAuthClient.login(realm, dto.username(), dto.senha(), clientId, clientSecret);
    }

    public void logout(AuthLogoutDTO dto) {
        keycloakAuthClient.logout(realm, dto.refreshToken(), clientId, clientSecret);
    }

}
