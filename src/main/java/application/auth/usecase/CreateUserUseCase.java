package application.auth.usecase;

import application.auth.service.AuthService;
import infrastructure.auth.dto.AuthCreateUserDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CreateUserUseCase {

    private final AuthService authService;

    @Inject
    public CreateUserUseCase(AuthService authService) {
        this.authService = authService;
    }

    public void execute(AuthCreateUserDTO dto) {
        authService.createUser(dto);
    }
}
