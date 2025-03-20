package application.usuario.usecase;

import application.usuario.mapper.UsuarioMapper;
import application.usuario.service.HashService;
import domain.usuario.model.Perfil;
import domain.usuario.model.Usuario;
import domain.usuario.repository.UsuarioRepository;
import exception.FormValidationException;
import infrastructure.usuario.dto.UsuarioRequestDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CreateUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final HashService hashService;

    @Inject
    public CreateUsuarioUseCase(final UsuarioRepository usuarioRepository, final UsuarioMapper usuarioMapper,
            final HashService hashService) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.hashService = hashService;
    }

    @Transactional
    public Long execute(final UsuarioRequestDTO usuarioRequestDTO) {

        validateInsert(usuarioRequestDTO);

        final Usuario usuario = this.usuarioMapper.toModel(usuarioRequestDTO);

        usuario.setUsername(usuarioRequestDTO.username());
        usuario.setName(usuarioRequestDTO.name());
        usuario.setCpf(usuarioRequestDTO.cpf());
        usuario.setEmail(usuarioRequestDTO.email());
        usuario.setSenha(hashService.getHashSenha(usuarioRequestDTO.senha()));
        usuario.setPerfil(Perfil.fromChar(usuarioRequestDTO.perfil().charAt(0)));

        this.usuarioRepository.save(usuario);

        return usuario.getId();
    }

    private void validateInsert(final UsuarioRequestDTO usuarioRequestDTO) {

        if (usuarioRequestDTO.perfil().length() != 1) {
            throw new FormValidationException("O perfil informado é inválido.");
        }
        try {
            Perfil.fromChar(usuarioRequestDTO.perfil().charAt(0));
        } catch (IllegalArgumentException e) {
            throw new FormValidationException("O perfil informado é inválido.");
        }
    }
}
