package application.usuario.service;

import java.util.List;

import org.keycloak.representations.idm.UserRepresentation;

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

    Usuario findByEmail(String email);

    Usuario findByCpf(String cpf);

    void deleteUsuario(Long id);

    void alterarSenha(String email, ResetPasswordResponseDTO resetPasswordResponseDTO);

    List<UserRepresentation> findKeycloakUsersByEmail(String email);
}
