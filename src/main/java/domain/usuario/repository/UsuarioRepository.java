package domain.usuario.repository;

import java.util.List;

import domain.usuario.model.Usuario;

public interface UsuarioRepository {

    List<Usuario> findAll();

    Usuario findById(Long id);

    void save(Usuario usuario);

    void deleteById(Long id);

    Usuario findByEmail(String email);

    Usuario findByCpf(String cpf);

    Usuario findByUsername(String username);

    Usuario findByUsernameAndSenha(String username, String senha);
}
