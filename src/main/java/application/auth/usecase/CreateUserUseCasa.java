package application.auth.usecase;

import application.auth.service.AuthService;
import infrastructure.auth.dto.AuthCreateUserDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CreateUserUseCasa {

    private final AuthService authService;

    @Inject
    public CreateUserUseCasa(AuthService authService) {
        this.authService = authService;
    }

    public void execute(AuthCreateUserDTO dto) {
        authService.createUser(dto);
    }
}
