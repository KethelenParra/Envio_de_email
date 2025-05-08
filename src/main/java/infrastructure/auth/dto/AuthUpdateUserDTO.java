package infrastructure.auth.dto;

public record AuthUpdateUserDTO(
        String email,
        Boolean emailVerified,
        Boolean enabled,
        String firstName,
        String lastName) {
}
