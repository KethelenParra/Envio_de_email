package application.auth.usecase;

import application.auth.service.AuthService;
import infrastructure.auth.dto.AuthLogoutDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class LogoutUseCase {

    private final AuthService authService;

    @Inject
    public LogoutUseCase(AuthService authService) {
        this.authService = authService;
    }

    public void execute(String accessToken, AuthLogoutDTO dto) {
        authService.logout(dto);

        authService.revokeAccessToken(accessToken);
    }

}
