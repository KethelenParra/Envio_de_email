package application.auth.service;

import java.util.List;

import org.keycloak.representations.idm.UserRepresentation;

import infrastructure.auth.dto.AuthCreateUserDTO;
import infrastructure.auth.dto.AuthLoginDTO;
import infrastructure.auth.dto.AuthLogoutDTO;
import infrastructure.auth.dto.AuthResetPasswordUserDTO;
import infrastructure.auth.dto.AuthUpdateUserDTO;
import infrastructure.auth.dto.TokenResponseDTO;

public interface AuthService {
    String createUser(AuthCreateUserDTO dto);

    void updateUser(String id, AuthUpdateUserDTO dto);

    void deleteUser(String id);

    void resetPassword(String id, AuthResetPasswordUserDTO dto);

    TokenResponseDTO login(AuthLoginDTO dto);

    void logout(AuthLogoutDTO dto);

    void assignRealmRoles(String userId, List<String> roles);

    String findKeycloakIdByUsername(String username);

    List<UserRepresentation> findUsersByEmail(String email);
}
