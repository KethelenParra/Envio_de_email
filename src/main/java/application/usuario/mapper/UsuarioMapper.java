package application.usuario.mapper;

import domain.usuario.model.Perfil;
import domain.usuario.model.Usuario;
import infrastructure.usuario.dto.UsuarioRequestDTO;
import jakarta.enterprise.context.ApplicationScoped;
import infrastructure.usuario.dto.UsuarioResponseDTO;

@ApplicationScoped
public class UsuarioMapper {

    public UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getName(),
                usuario.getCpf(),
                usuario.getEmail(),
                usuario.getPerfil().getTipo());
    }

    public Usuario toModel(final UsuarioRequestDTO dto) {
        return new Usuario(
                dto.username(),
                dto.name(),
                dto.cpf(),
                dto.email(),
                Perfil.fromChar(dto.perfil().charAt(0)));
    }

    public UsuarioRequestDTO toDTORequest(final Usuario model) {
        return new UsuarioRequestDTO(
                model.getUsername(),
                model.getName(),
                model.getCpf(),
                model.getEmail(),
                String.valueOf(model.getPerfil().getTipo()),
                model.getSenha());
    }
}
