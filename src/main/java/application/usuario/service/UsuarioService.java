package application.usuario.service;

import java.util.List;

import domain.usuario.model.Usuario;
import infrastructure.usuario.dto.ResetPasswordResponseDTO;
import infrastructure.usuario.dto.UsuarioRequestDTO;

public interface UsuarioService {

    Usuario getUsuarioById(Long id);

    List<Usuario> getAllUsuarios();

    Long createUsuario(UsuarioRequestDTO usuarioRequestDTO);

    UsuarioRequestDTO updateUsuario(Long id, UsuarioRequestDTO usuarioRequestDTO);

    Usuario findByUsernameAndSenha(String username, String senha);

    Usuario findByUsername(String username);

    void deleteUsuario(Long id);

    void alterarSenha(Long userId, ResetPasswordResponseDTO resetPasswordResponseDTO);
}
