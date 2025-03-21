package application.usuario.usecase;

import application.usuario.service.HashService;
import domain.usuario.model.Usuario;
import domain.usuario.repository.UsuarioRepository;
import exception.FormValidationException;
import infrastructure.email.EmailService.EmailServiceImpl;
import infrastructure.usuario.dto.ResetPasswordResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;

@ApplicationScoped
public class ResetPasswordUseCase {
    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    HashService hashService;

    @Inject
    EmailServiceImpl emailService;

    @Transactional
    public void alterarSenha(Long userId, ResetPasswordResponseDTO dto) {
        Usuario usuario = usuarioRepository.findById(userId);
        if (usuario == null) {
            throw new FormValidationException("Usuário não encontrado.");
        }

        if (usuario.getSenha() == null) {
            throw new ValidationException("Senha do usuário não está definida.");
        }

        boolean senhaAntigaCorreta = hashService.verificandoHash(dto.senhaAntiga(), usuario.getSenha());

        if (!senhaAntigaCorreta) {
            throw new ValidationException("Senha antiga não confere.");
        }

        if (!dto.novaSenha().equals(dto.confirmacaoNovaSenha())) {
            throw new ValidationException("A confirmação da nova senha não confere.");
        }

        usuario.setSenha(hashService.getHashSenha(dto.novaSenha()));

        this.usuarioRepository.save(usuario);

        // Enviar e-mail de confirmação da alteração de senha (Adicionar)

        String linkRedefinicao = "https://redefinir-senha-teste.com";
        int tempoExpiracao = 30;

        ((EmailServiceImpl) emailService).sendResetPasswordEmail(
                "kethelenvictoria2016@gmail.com",
                usuario.getName(),
                linkRedefinicao,
                tempoExpiracao);
    }
}
