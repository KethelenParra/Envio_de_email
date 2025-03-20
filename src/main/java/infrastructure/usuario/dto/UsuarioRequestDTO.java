package infrastructure.usuario.dto;

import jakarta.validation.constraints.NotBlank;

public record UsuarioRequestDTO(

        @NotBlank(message = "O campo username não pode ser nulo.") String username,

        @NotBlank(message = "O campo nome não pode ser nulo.") String name,

        @NotBlank(message = "O campo cpf não pode ser nulo.") String cpf,

        @NotBlank(message = "O campo email não pode ser nulo.") String email,

        @NotBlank(message = "O campo perfil não pode ser nulo.") String perfil,

        String senha) {
}
