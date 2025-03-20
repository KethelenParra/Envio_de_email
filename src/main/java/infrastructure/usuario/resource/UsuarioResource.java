package infrastructure.usuario.resource;

import java.util.List;
import java.util.stream.Collectors;
import application.usuario.mapper.UsuarioMapper;
import application.usuario.service.UsuarioService;
import infrastructure.usuario.dto.UsuarioRequestDTO;
import infrastructure.usuario.dto.UsuarioResponseDTO;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

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
    public UsuarioRequestDTO findUsuarioById(@PathParam("id") final Long id) {
        return this.usuarioMapper.toDTORequest(this.usuarioService.getUsuarioById(id));
    }

    @DELETE
    @Path("/{id}")
    public void deleteUsuario(@PathParam("id") final Long id) {
        this.usuarioService.deleteUsuario(id);
    }

    @GET
    @RolesAllowed("GERENTE")
    public List<UsuarioResponseDTO> getAllUsuarios() {
        return this.usuarioService.getAllUsuarios().stream()
                .map(usuarioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
