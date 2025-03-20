package infrastructure.usuario.dto;

import domain.usuario.model.Perfil;
import domain.usuario.model.Usuario;

public record UsuarioLoginResponseDTO(
        Long id,
        String username,
        Perfil perfil) {
    public static UsuarioLoginResponseDTO valueOf(Usuario usuario) {
        return new UsuarioLoginResponseDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getPerfil());
    }
}