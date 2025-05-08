package application.auth.usecase;

import application.auth.service.AuthService;
import infrastructure.auth.dto.AuthUpdateUserDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UpdateUserUseCase {

    private final AuthService authService;

    @Inject
    public UpdateUserUseCase(AuthService authService) {
        this.authService = authService;
    }

    public void execute(String id, AuthUpdateUserDTO dto) {
        authService.updateUser(id, dto);
    }
}
