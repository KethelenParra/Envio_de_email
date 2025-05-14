package infrastructure.usuario.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.keycloak.representations.idm.UserRepresentation;

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
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("/api/usuario")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    @Inject
    SecurityContext securityContext;

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
    @RolesAllowed("GERENTE")
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
    @RolesAllowed({ "GERENTE", "DESENVOLVEDOR" })
    public List<UsuarioResponseDTO> getAllUsuarios() {
        return this.usuarioService.getAllUsuarios().stream()
                .map(usuarioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @PUT
    @Path("/alterar-senha")
    @RolesAllowed({ "GERENTE", "DESENVOLVEDOR" })
    public Response alterarSenha(@Valid ResetPasswordResponseDTO dto) {
        JsonWebToken jwt = (JsonWebToken) securityContext.getUserPrincipal();
        String email = jwt.getClaim("email");

        usuarioService.alterarSenha(email, dto);
        return Response.noContent().build();
    }

    @GET
    @Path("/email/{email}")
    @RolesAllowed({ "GERENTE", "DESENVOLVEDOR" })
    public Response findByEmail(@PathParam("email") String email) {
        // 1) Busca no banco
        Usuario usuario = usuarioService.findByEmail(email);

        // 2) Busca no Keycloak
        List<UserRepresentation> kcUsers = usuarioService.findKeycloakUsersByEmail(email);

        // 3) Monta o retorno
        Map<String, Object> result = new HashMap<>();
        result.put("local", usuario);
        result.put("keycloak", kcUsers);

        return Response.ok(result).build();
    }
}
