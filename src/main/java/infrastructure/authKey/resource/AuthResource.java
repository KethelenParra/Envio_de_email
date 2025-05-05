package infrastructure.authKey.resource;

import java.util.Map;

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
    @ConfigProperty(name = "keycloak.client-id")
    String clientId;

    @ConfigProperty(name = "keycloak.client-secret")
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

            AccessTokenResponse tokenResponse = kc.tokenManager()
                    .getAccessToken();

            TokenResponseDTO dto = TokenResponseDTO.from(tokenResponse);
            return Response.ok(dto).build();

        } catch (Exception e) {
            return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity(Map.of("error", "Usuário ou senha inválidos"))
                    .build();
        }
    }
}
