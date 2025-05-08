package application.auth.usecase;

import application.auth.service.AuthService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DeleteUserUseCase {

    private final AuthService authService;

    @Inject
    public DeleteUserUseCase(AuthService authService) {
        this.authService = authService;
    }

    public void execute(String id) {
        authService.deleteUser(id);
    }
}