package application.auth.usecase;

import application.auth.service.AuthService;
import infrastructure.auth.dto.AuthLoginDTO;
import infrastructure.auth.dto.TokenResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class LoginUseCase {

    private final AuthService authService;

    @Inject
    public LoginUseCase(AuthService authService) {
        this.authService = authService;
    }

    public TokenResponseDTO execute(AuthLoginDTO dto) {
        return authService.login(dto);
    }
}
