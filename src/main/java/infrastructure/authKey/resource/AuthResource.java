package infrastructure.authKey.resource;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;

import infrastructure.authKey.dto.LoginRequestDTO;
import infrastructure.authKey.dto.TokenResponseDTO;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @ConfigProperty(name = "keycloak.server-url")
    String serverUrl;

    @ConfigProperty(name = "keycloak.realm")
    String realm;

    // clientId + secret vindos do quarkus.oidc
    @ConfigProperty(name = "quarkus.oidc.client-id")
    String clientId;

    @ConfigProperty(name = "quarkus.oidc.credentials.secret")
    String clientSecret;

    @POST
    @Path("/login")
    public Response login(LoginRequestDTO loginRequestDTO) {

        try (Keycloak kc = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .grantType(OAuth2Constants.PASSWORD)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .username(loginRequestDTO.username())
                .password(loginRequestDTO.senha())
                .build()) {

            AccessTokenResponse atr = kc.tokenManager().getAccessToken();
            return Response.ok(TokenResponseDTO.from(atr)).build();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao autenticar usuário: " + e.getMessage(), e);
        }
    }
}
