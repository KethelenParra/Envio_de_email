package application.usuario.service;

import domain.usuario.model.Usuario;
import infrastructure.usuario.dto.UsuarioRequestDTO;

public interface UsuarioService {

    Usuario getUsuarioById(Long id);

    Long createUsuario(UsuarioRequestDTO usuarioRequestDTO);

    UsuarioRequestDTO updateUsuario(Long id, UsuarioRequestDTO usuarioRequestDTO);

    void deleteUsuario(Long id);
}
