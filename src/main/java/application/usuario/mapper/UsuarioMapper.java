package application.usuario.mapper;

import domain.usuario.model.Perfil;
import domain.usuario.model.Usuario;
import infrastructure.usuario.dto.UsuarioRequestDTO;
import jakarta.enterprise.context.ApplicationScoped;
import infrastructure.usuario.dto.UsuarioResponseDTO;

@ApplicationScoped
public class UsuarioMapper {

    public infrastructure.usuario.dto.UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.isAtivo(),
                usuario.getPerfil().getTipo());
    }

    public Usuario toModel(
            final UsuarioRequestDTO dto) {
        return new Usuario(
                dto.id(),
                dto.username(),
                dto.isAtivo(),
                Perfil.fromChar(dto.perfil().charAt(0)));
    }

    public UsuarioRequestDTO toDTORequest(final Usuario model) {
        return new UsuarioRequestDTO(
                model.getId(),
                model.getUsername(),
                model.isAtivo(),
                String.valueOf(model.getPerfil().getTipo()));
    }

}
