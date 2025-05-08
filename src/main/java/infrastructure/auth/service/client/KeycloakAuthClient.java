package infrastructure.auth.service.client;

import infrastructure.auth.dto.AuthCreateUserDTO;
import infrastructure.auth.dto.AuthResetPasswordUserDTO;
import infrastructure.auth.dto.AuthUpdateUserDTO;
import infrastructure.auth.dto.TokenResponseDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "keycloak-api")
@Path("/admin/realms/{realm}")
public interface KeycloakAuthClient {

    @POST
    @Path("/users")
    @Consumes(MediaType.APPLICATION_JSON)
    Response createUser(@PathParam("realm") String realm, AuthCreateUserDTO userDto);

    @PUT
    @Path("/users/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    Response updateUser(@PathParam("realm") String realm, @PathParam("id") String userId, AuthUpdateUserDTO userDto);

    @DELETE
    @Path("/users/{id}")
    Response deleteUser(@PathParam("realm") String realm, @PathParam("id") String userId);

    @PUT
    @Path("/users/{id}/reset-password")
    @Consumes(MediaType.APPLICATION_JSON)
    Response resetPassword(@PathParam("realm") String realm, @PathParam("id") String userId,
            AuthResetPasswordUserDTO dto);

    @POST
    @Path("/protocol/openid-connect/token")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    TokenResponseDTO login(
            @PathParam("realm") String realm,
            @FormParam("username") String username,
            @FormParam("password") String password,
            @FormParam("client_id") String clientId,
            @FormParam("client_secret") String clientSecret);

    @POST
    @Path("/protocol/openid-connect/logout")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    void logout(
            @PathParam("realm") String realm,
            @FormParam("refresh_token") String refreshToken,
            @FormParam("client_id") String clientId,
            @FormParam("client_secret") String clientSecret);
}
