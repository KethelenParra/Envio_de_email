package infrastructure.auth.service.client;

import infrastructure.auth.dto.AuthCreateUserDTO;
import infrastructure.auth.dto.AuthResetPasswordUserDTO;
import infrastructure.auth.dto.AuthUpdateUserDTO;
import infrastructure.auth.dto.TokenResponseDTO;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.keycloak.representations.idm.RoleRepresentation;

@RegisterRestClient(configKey = "keycloak-api")
public interface KeycloakAuthClient {

    String REALM = "/realms/{realm}";
    String ADMIN_REALM = "/admin" + REALM;
    String USERS = ADMIN_REALM + "/users";
    String ROLES = ADMIN_REALM + "/roles";

    @POST
    @Path(USERS)
    @Consumes(MediaType.APPLICATION_JSON)
    Response createUser(
            @HeaderParam("Authorization") String bearerToken,
            @PathParam("realm") String realm,
            AuthCreateUserDTO dto);

    @PUT
    @Path(USERS + "/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    Response updateUser(@HeaderParam("Authorization") String bearerToken,
            @PathParam("realm") String realm,
            @PathParam("id") String id,
            AuthUpdateUserDTO dto);

    @DELETE
    @Path(USERS + "/{id}")
    Response deleteUser(@HeaderParam("Authorization") String bearerToken,
            @PathParam("realm") String realm,
            @PathParam("id") String id);

    @PUT
    @Path(USERS + "/{id}/reset-password")
    @Consumes(MediaType.APPLICATION_JSON)
    Response resetPassword(@HeaderParam("Authorization") String bearerToken,
            @PathParam("realm") String realm,
            @PathParam("id") String id,
            AuthResetPasswordUserDTO dto);

    @POST
    @Path(REALM + "/protocol/openid-connect/token")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    TokenResponseDTO login(@PathParam("realm") String realm,
            @FormParam("grant_type") String grantType,
            @FormParam("username") String username,
            @FormParam("password") String password,
            @FormParam("client_id") String clientId,
            @FormParam("client_secret") String clientSecret);

    @POST
    @Path(REALM + "/protocol/openid-connect/logout")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    void logout(@PathParam("realm") String realm,
            @FormParam("refresh_token") String refreshToken,
            @FormParam("client_id") String clientId,
            @FormParam("client_secret") String clientSecret);

    @GET
    @Path(ROLES + "/{role-name}")
    @Produces(MediaType.APPLICATION_JSON)
    RoleRepresentation getRealmRole(@HeaderParam("Authorization") String bearerToken,
            @PathParam("realm") String realm,
            @PathParam("role-name") String roleName);

    @POST
    @Path(USERS + "/{id}/role-mappings/realm")
    @Consumes(MediaType.APPLICATION_JSON)
    Response addRealmRoleMapping(@HeaderParam("Authorization") String bearerToken,
            @PathParam("realm") String realm,
            @PathParam("id") String userId,
            List<RoleRepresentation> roles);
}
