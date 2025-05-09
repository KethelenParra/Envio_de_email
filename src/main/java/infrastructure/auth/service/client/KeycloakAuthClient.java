package infrastructure.auth.service.client;

import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import infrastructure.auth.dto.AuthCreateUserDTO;
import infrastructure.auth.dto.AuthResetPasswordUserDTO;
import infrastructure.auth.dto.AuthUpdateUserDTO;
import infrastructure.auth.dto.TokenResponseDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RegisterRestClient(configKey = "keycloak-api")
@RegisterClientHeaders(KeycloakAuthClient.KeycloakAuthHeaderFactory.class)
public interface KeycloakAuthClient {

    // final static String path = "/realms/{realm}";
    // final static String pathUsers = "/admin/realms/{realm}/users";

    @POST
    @Path("/admin/realms/{realm}/users")
    @Consumes(MediaType.APPLICATION_JSON)
    Response createUser(@PathParam("realm") String realm, AuthCreateUserDTO userDto);

    @PUT
    @Path("/admin/realms/{realm}/users/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    Response updateUser(@PathParam("realm") String realm, @PathParam("id") String userId, AuthUpdateUserDTO userDto);

    @DELETE
    @Path("/admin/realms/{realm}/users/{id}")
    Response deleteUser(@PathParam("realm") String realm, @PathParam("id") String userId);

    @PUT
    @Path("/admin/realms/{realm}/users/{id}/reset-password")
    @Consumes(MediaType.APPLICATION_JSON)
    Response resetPassword(@PathParam("realm") String realm, @PathParam("id") String userId,
            AuthResetPasswordUserDTO dto);

    @POST
    @Path("/realms/{realm}/protocol/openid-connect/token")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    TokenResponseDTO login(
            @PathParam("realm") String realm,
            @FormParam("grant_type") String grantType,
            @FormParam("username") String username,
            @FormParam("password") String password,
            @FormParam("client_id") String clientId,
            @FormParam("client_secret") String clientSecret);

    @POST
    @Path("/realms/{realm}/protocol/openid-connect/logout")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    void logout(
            @PathParam("realm") String realm,
            @FormParam("refresh_token") String refreshToken,
            @FormParam("client_id") String clientId,
            @FormParam("client_secret") String clientSecret);

}
