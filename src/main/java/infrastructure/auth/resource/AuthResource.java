package infrastructure.auth.resource;

import java.util.Collections;
import java.util.Map;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;

import application.auth.usecase.LoginUseCase;
import application.auth.usecase.LogoutUseCase;
import infrastructure.auth.dto.AuthLoginDTO;
import infrastructure.auth.dto.AuthLogoutDTO;
import infrastructure.auth.dto.TokenResponseDTO;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;

@Path("/api/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    LoginUseCase loginUseCase;

    @Inject
    LogoutUseCase logoutUseCase;

    @POST
    @Path("/login")
    public Response login(AuthLoginDTO dto) {
        try {
            TokenResponseDTO token = loginUseCase.execute(dto);

            NewCookie jwtCookie = new NewCookie(
                    /* name */ "jwt_token",
                    /* value */ token.accessToken(), // String
                    /* path */ "/",
                    /* domain */ null,
                    /* comment */ "JWT Token",
                    /* maxAge */ NewCookie.DEFAULT_MAX_AGE, // int
                    /* secure */ false, // HTTPS only? depois mude para true
                    /* httpOnly */ false // JS inacessível? depois mude para true
            );

            return Response.ok(token)
                    .cookie(jwtCookie)
                    .build();

        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(Collections.singletonMap("error", e.getMessage()))
                    .build();
        }
    }

    @POST
    @Path("/logout")
    public Response logout(
            @CookieParam("jwt_token") Cookie jwtCookie,
            AuthLogoutDTO dto) {

        if (jwtCookie == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"jwt_token cookie not found\"}")
                    .build();
        }

        // cookie.value() já é o access token puro
        String accessToken = jwtCookie.getValue();

        logoutUseCase.execute(accessToken, dto);
        NewCookie clear = new NewCookie(
                "jwt_token", // name
                "", // value vazio
                "/", // path igual
                null, // domain igual
                null, // comentário (pode ser null)
                0, // maxAge=0 para expirar imediatamente
                false, // secure = false
                false // httpOnly = false
        );
        return Response.noContent()
                .cookie(clear)
                .build();
    }
}
