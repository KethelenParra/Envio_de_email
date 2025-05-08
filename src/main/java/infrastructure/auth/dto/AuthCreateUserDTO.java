package infrastructure.auth.dto;

public record AuthCreateUserDTO(
        String username,
        String email,
        Boolean emailVerified,
        Boolean enabled,
        String firstName,
        String lastName) {

}
