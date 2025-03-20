package infrastructure.filter;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.ext.Provider;

@Provider
public class TokenResponseFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        Object newTokenObj = requestContext.getProperty("newToken");
        if (newTokenObj != null && newTokenObj instanceof String) {
            String newToken = (String) newTokenObj;
            NewCookie jwtCookie = new NewCookie(
                    "jwt_token", newToken, "/", null, "JWT Token",
                    (int) infrastructure.auth.service.JwtServiceImpl.EXPIRATION_TIME.toSeconds(),
                    true,
                    true);
            // Adiciona o cookie com o novo token na resposta
            responseContext.getHeaders().add("Set-Cookie", jwtCookie.toString());
        }
    }
}