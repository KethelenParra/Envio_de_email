package infrastructure.auth.resource;

import java.time.Instant;

import org.eclipse.microprofile.jwt.JsonWebToken;

import application.filter.NotAuthenticate;
import application.usuario.service.HashService;
import application.usuario.service.JwtService;
import application.usuario.service.UsuarioService;
import domain.usuario.model.Usuario;
import infrastructure.auth.dto.AuthRequestDTO;
import infrastructure.auth.service.JwtServiceImpl;
import infrastructure.usuario.dto.UsuarioLoginResponseDTO;
import io.smallrye.jwt.auth.principal.DefaultJWTParser;
import io.smallrye.jwt.auth.principal.JWTCallerPrincipal;
import io.smallrye.jwt.auth.principal.ParseException;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.Response.Status;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    HashService hashService;

    @Inject
    UsuarioService usuarioService;

    @Inject
    JwtService jwtService;

    @Inject
    DefaultJWTParser jwtParser;

    @POST
    @NotAuthenticate
    public Response login(AuthRequestDTO authDTO) {
        String hash = hashService.getHashSenha(authDTO.senha());

        Usuario usuario = usuarioService.findByUsernameAndSenha(authDTO.username(), hash);

        if (usuario == null) {
            return Response.status(Status.UNAUTHORIZED)
                    .entity("Usuário ou senha incorretos").build();
        }

        String token = jwtService.generateJwt(UsuarioLoginResponseDTO.valueOf(usuario));

        NewCookie jwtCookie = new NewCookie(
                "jwt_token", token, "/", null, "JWT Token",
                (int) JwtServiceImpl.EXPIRATION_TIME.toSeconds(),
                false,
                false

        );

        return Response.ok()
                .entity("{\"mensagem\": \"Login bem-sucedido\", \"token\": \"" + token + "\"}")
                .cookie(jwtCookie)
                .build();

    }

    @POST
    @Path("/logout")
    public Response logout() {
        NewCookie expiredCookie = new NewCookie(
                "jwt_token", "", "/", null, "Token Removido",
                0,
                false, // DEVE SER TRUE QUANDO O SERVIDOR ESTIVER COM HTTPS
                false // DEVE SER TRUE QUANDO O SERVIDOR ESTIVER COM HTTPS
        );

        return Response.ok("Logout realizado com sucesso!").cookie(expiredCookie).build();
    }

    // metodo temporario apenas para testes
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProtectedData(@CookieParam("jwt_token") String token) {
        if (token == null || token.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Acesso negado").build();
        }

        return Response.ok().entity("{\"mensagem\": \"Dados protegidos acessados!\"}").build();
    }

    @GET
    @Path("/whoami")
    @RolesAllowed({ "GERENTE", "DESENVOLVEDOR" })
    public Response whoAmI(@Context SecurityContext securityContext) {

        if (securityContext.getUserPrincipal() == null) {
            return Response.ok("Nenhum usuário logado no momento").build();
        }

        JWTCallerPrincipal jwtPrincipal = (JWTCallerPrincipal) securityContext.getUserPrincipal();

        String username = jwtPrincipal.getName();

        return Response.ok("Usuário logado: " + username).build();
    }

    // Fiz duas formas para visualizar quem esta logado, uma pelo jwt via cookie e
    // outro pelo jwt via Authorize no Swagger. Para aprendizado mesmo

    @GET
    @Path("/whoami-cookie")
    public Response whoAmI(@CookieParam("jwt_token") String token) {
        if (token == null || token.isEmpty()) {
            return Response.ok("Nenhum usuário logado no momento (cookie vazio)").build();
        }

        try {

            JsonWebToken jwt = jwtParser.parse(token);

            // 3. Verifica se está expirado
            long expirationTime = jwt.getExpirationTime();
            long currentTime = Instant.now().getEpochSecond();
            if (expirationTime <= currentTime) {
                return Response.ok("Nenhum usuário logado (token expirado)").build();
            }

            // 4. Se chegou aqui, token é válido. Extrai o subject (ou 'upn', etc.)
            String username = jwt.getSubject(); // ou getName(), dependendo das claims

            // 5. Retorna info de quem está logado
            return Response.ok("Usuário logado: " + username).build();

        } catch (ParseException e) {
            return Response.ok("Nenhum usuário logado (token inválido)").build();
        }
    }
}