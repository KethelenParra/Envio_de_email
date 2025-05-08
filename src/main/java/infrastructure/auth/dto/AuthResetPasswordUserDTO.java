package infrastructure.auth.dto;

public record AuthResetPasswordUserDTO(
        String type,
        String value,
        Boolean temporary) {
}
