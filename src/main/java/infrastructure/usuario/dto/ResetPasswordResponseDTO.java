package infrastructure.usuario.dto;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordResponseDTO(
        @NotBlank(message = "senha antiga não pode ser nulo") String senhaAntiga,
        @NotBlank(message = "nova senha não pode ser nulo") String novaSenha,
        @NotBlank(message = "confirmação nova senha não pode ser nulo") String confirmacaoNovaSenha) {
}
