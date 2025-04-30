package infrastructure.usuario.resource;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;

import application.usuario.mapper.UsuarioMapper;
import application.usuario.service.UsuarioService;
import domain.usuario.model.Usuario;
import infrastructure.usuario.dto.ResetPasswordResponseDTO;
import infrastructure.usuario.dto.UsuarioRequestDTO;
import infrastructure.usuario.dto.UsuarioResponseDTO;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/usuario")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    @Inject
    JWTParser jwtParser;

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    @Inject
    public UsuarioResource(final UsuarioService usuarioService, final UsuarioMapper usuarioMapper) {
        this.usuarioService = usuarioService;
        this.usuarioMapper = usuarioMapper;
    }

    @POST
    @RolesAllowed("GERENTE")
    public Long createUsuario(@Valid final UsuarioRequestDTO usuarioRequestDTO) {
        return this.usuarioService.createUsuario(usuarioRequestDTO);
    }

    @PUT
    @Path("/{id}")
    public UsuarioRequestDTO updateUsuario(@PathParam("id") final Long id,
            @Valid final UsuarioRequestDTO usuarioRequestDTO) {
        return this.usuarioService.updateUsuario(id, usuarioRequestDTO);
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("GERENTE")
    public UsuarioRequestDTO findUsuarioById(@PathParam("id") final Long id) {
        return this.usuarioMapper.toDTORequest(this.usuarioService.getUsuarioById(id));
    }

    @DELETE
    @Path("/{id}")
    public void deleteUsuario(@PathParam("id") final Long id) {
        this.usuarioService.deleteUsuario(id);
    }

    @GET
    @SecurityRequirement(name = "bearer-jwt")
    @RolesAllowed({ "GERENTE", "DESENVOLVEDOR" })
    public List<UsuarioResponseDTO> getAllUsuarios() {
        return this.usuarioService.getAllUsuarios().stream()
                .map(usuarioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @PUT
    @Path("/alterar-senha")
    public Response alterarSenha(@CookieParam("jwt_token") String token,
            @Valid ResetPasswordResponseDTO dto) {
        if (token == null || token.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Usuário não autenticado")
                    .build();
        }

        try {
            JsonWebToken jwt = jwtParser.parse(token);
            String username = jwt.getSubject();

            Usuario usuario = usuarioService.findByUsername(username);
            if (usuario == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Usuário não encontrado")
                        .build();
            }

            usuarioService.alterarSenha(usuario.getId(), dto);

            return Response.ok("{\"mensagem\": \"Senha alterada com sucesso\"}").build();
        } catch (ParseException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Token inválido")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Erro ao alterar senha: " + e.getMessage())
                    .build();
        }
    }
}
