package application.auth.usecase;

import application.auth.service.AuthService;
import infrastructure.auth.dto.AuthResetPasswordUserDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ResetPasswordUseCase {

    private final AuthService authService;

    @Inject
    public ResetPasswordUseCase(AuthService authService) {
        this.authService = authService;
    }

    public void execute(String id, AuthResetPasswordUserDTO dto) {
        authService.resetPassword(id, dto);
    }
}