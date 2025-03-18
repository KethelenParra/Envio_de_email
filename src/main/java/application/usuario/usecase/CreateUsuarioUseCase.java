package application.usuario.usecase;

import java.util.List;

import application.exception.FormValidationException;
import application.usuario.mapper.UsuarioMapper;
import domain.usuario.model.Perfil;
import domain.usuario.model.Usuario;
import domain.usuario.repository.UsuarioRepository;
import infrastructure.usuario.dto.UsuarioRequestDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CreateUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    private final UsuarioMapper usuarioMapper;

    @Inject
    public CreateUsuarioUseCase(
            final UsuarioRepository usuarioRepository,
            final UsuarioMapper usuarioMapper) {

        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    @Transactional
    public Long execute(final UsuarioRequestDTO usuarioRequestDTO) {

        this.validateInsert(usuarioRequestDTO);

        final Usuario usuario = this.usuarioMapper.toModel(
                usuarioRequestDTO);

        if (usuario.getSenha() == null || usuario.getSenha().isEmpty()) {
            usuario.setSenha(null);
        }

        this.usuarioRepository.save(usuario);

        return usuario.getId();
    }

    private void validateInsert(final UsuarioRequestDTO usuarioRequestDTO) {

        // Verifica se o perfil é válido
        if (usuarioRequestDTO.perfil().length() != 1 || Perfil.fromChar(usuarioRequestDTO.perfil().charAt(0)) == null) {
            throw new FormValidationException("O perfil informado é inválido.");
        }

        // Verifica se o status ativo/inativo está coerente
        if (!usuarioRequestDTO.isAtivo() && usuarioRequestDTO.perfil().charAt(0) == 'A') {
            throw new FormValidationException("Usuários inativos não podem ter perfil de administrador.");
        }

    }
}
