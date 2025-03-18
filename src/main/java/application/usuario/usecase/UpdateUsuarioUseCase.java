package application.usuario.usecase;

import application.exception.FormValidationException;
import domain.usuario.model.Perfil;
import domain.usuario.model.Usuario;
import domain.usuario.repository.UsuarioRepository;
import infrastructure.usuario.dto.UsuarioRequestDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UpdateUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    @Inject
    public UpdateUsuarioUseCase(
            final UsuarioRepository usuarioRepository) {

        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public UsuarioRequestDTO execute(final Long id, final UsuarioRequestDTO usuarioRequestDTO) {
        final Usuario usuario = this.usuarioRepository.findById(id);

        if (usuario == null) {
            throw new EntityNotFoundException("Usuário com ID " + id + " não encontrado.");
        }

        this.validateInsert(usuarioRequestDTO);

        usuario.setPerfil(Perfil.fromChar(usuarioRequestDTO.perfil().charAt(0)));
        usuario.setUsername(usuarioRequestDTO.username());
        usuario.setAtivo(usuarioRequestDTO.isAtivo());

        // Salva as mudanças no usuário
        this.usuarioRepository.save(usuario);
        return usuarioRequestDTO;
    }

    private void validateInsert(
            final UsuarioRequestDTO usuarioRequestDTO) {

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
