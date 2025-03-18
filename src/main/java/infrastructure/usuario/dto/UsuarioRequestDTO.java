package infrastructure.usuario.dto;

import jakarta.validation.constraints.NotBlank;

public record UsuarioRequestDTO(
        Long id,

        @NotBlank(message = "O campo username não pode ser nulo.") String username,

        Boolean isAtivo,

        @NotBlank(message = "O campo perfil não pode ser nulo.") String perfil) {
}
