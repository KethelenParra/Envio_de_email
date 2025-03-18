package infrastructure.usuario.resource;

import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import application.usuario.mapper.UsuarioMapper;
import application.usuario.service.UsuarioService;
import infrastructure.usuario.dto.UsuarioRequestDTO;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/usuario")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    private final UsuarioService usuarioService;

    private final UsuarioMapper usuarioMapper;

    @Inject
    public UsuarioResource(final UsuarioService usuarioService, final UsuarioMapper usuarioMapper) {
        this.usuarioService = usuarioService;
        this.usuarioMapper = usuarioMapper;
    }

    @POST
    public Long createUsuario(@Valid @RequestBody final UsuarioRequestDTO usuarioRequestDTO) {
        return this.usuarioService.createUsuario(usuarioRequestDTO);
    }

    @PUT
    @Path("/{id}")
    public UsuarioRequestDTO updateUsuario(@PathParam("id") final Long id,
            @Valid @RequestBody final UsuarioRequestDTO usuarioRequestDTO) {
        return this.usuarioService.updateUsuario(id, usuarioRequestDTO);
    }

    @GET
    @Path("/{id}")
    public UsuarioRequestDTO findUsuarioById(@PathParam("id") final Long id) {
        return this.usuarioMapper.toDTORequest(this.usuarioService.getUsuarioById(id));
    }

    @GET
    @Path("/cpf/{cpf}")
    public Response validateCpf(@PathParam("cpf") final String cpf) {
        return Response.ok().entity(this.usuarioService.validateCpf(cpf)).build();
    }

}
