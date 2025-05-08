package application.auth.service;

import infrastructure.auth.dto.AuthCreateUserDTO;
import infrastructure.auth.dto.AuthLoginDTO;
import infrastructure.auth.dto.AuthLogoutDTO;
import infrastructure.auth.dto.AuthResetPasswordUserDTO;
import infrastructure.auth.dto.AuthUpdateUserDTO;
import infrastructure.auth.dto.TokenResponseDTO;

public interface AuthService {
    void createUser(AuthCreateUserDTO dto);

    void updateUser(String id, AuthUpdateUserDTO dto);

    void deleteUser(String id);

    void resetPassword(String id, AuthResetPasswordUserDTO dto);

    TokenResponseDTO login(AuthLoginDTO dto);

    void logout(AuthLogoutDTO dto);

}
