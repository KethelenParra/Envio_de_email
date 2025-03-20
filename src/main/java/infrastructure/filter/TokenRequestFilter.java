package infrastructure.filter;

import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import org.eclipse.microprofile.jwt.JsonWebToken;

import application.filter.NotAuthenticate;
import application.usuario.service.JwtService;
import application.usuario.service.UsuarioService;
import domain.usuario.model.Usuario;
import infrastructure.usuario.dto.UsuarioLoginResponseDTO;
import io.smallrye.jwt.auth.principal.DefaultJWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.Provider;

import java.time.Instant;

@Provider
public class TokenRequestFilter implements ContainerRequestFilter {

    @Inject
    DefaultJWTParser jwtParser;

    @Context
    private ResourceInfo resourceInfo;

    @Inject
    JwtService jwtService;

    @Inject
    UsuarioService usuarioService;

    @Override
    public void filter(ContainerRequestContext requestContext) {

        if (resourceInfo.getResourceMethod().isAnnotationPresent(NotAuthenticate.class) ||
                resourceInfo.getResourceClass().isAnnotationPresent(NotAuthenticate.class)) {
            return;
        }

        Cookie jwtCookie = requestContext.getCookies().get("jwt_token");

        if (jwtCookie == null) {
            requestContext.abortWith(Response.status(Status.UNAUTHORIZED)
                    .entity("Token não encontrado")
                    .build());
            return;
        }

        String token = jwtCookie.getValue();

        try {
            JsonWebToken jwt = jwtParser.parse(token);

            long expirationTime = jwt.getExpirationTime();
            long currentTime = Instant.now().getEpochSecond();

            if (expirationTime <= currentTime) {
                requestContext.abortWith(Response.status(Status.UNAUTHORIZED)
                        .entity("Token expirado")
                        .build());
            }

            Object iatClaim = jwt.getClaim("iat");
            if (iatClaim != null) {
                long issuedAt = Long.parseLong(iatClaim.toString());
                long tokenAge = currentTime - issuedAt;

                if (tokenAge >= 300) {
                    Usuario usuario = usuarioService.findByUsername(jwt.getSubject());
                    String newToken = jwtService.generateJwt(UsuarioLoginResponseDTO.valueOf(usuario));

                    requestContext.setProperty("newToken", newToken);
                }
            }

        } catch (ParseException e) {
            requestContext.abortWith(Response.status(Status.UNAUTHORIZED)
                    .entity("Token inválido")
                    .build());

        }
    }
}
