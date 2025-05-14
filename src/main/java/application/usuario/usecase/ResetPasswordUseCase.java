package application.usuario.usecase;

import application.usuario.service.HashService;
import domain.usuario.model.Usuario;
import domain.usuario.repository.UsuarioRepository;
import exception.FormValidationException;
import infrastructure.email.EmailService.EmailServiceImpl;
import infrastructure.usuario.dto.ResetPasswordResponseDTO;
import application.auth.service.AuthService;
import infrastructure.auth.dto.AuthResetPasswordUserDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ResetPasswordUseCase {
    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    HashService hashService;

    @Inject
    EmailServiceImpl emailService;

    @Inject
    AuthService authService;

    @Transactional
    public void alterarSenha(String email, ResetPasswordResponseDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            throw new FormValidationException("Usuário não encontrado.");
        }

        if (usuario.getSenha() == null) {
            throw new FormValidationException("Senha do usuário não está definida.");
        }

        boolean senhaAntigaCorreta = hashService.verificandoHash(dto.senhaAntiga(), usuario.getSenha());

        if (!senhaAntigaCorreta) {
            throw new FormValidationException("Senha antiga não confere.");
        }

        if (!dto.novaSenha().equals(dto.confirmacaoNovaSenha())) {
            throw new FormValidationException("A confirmação da nova senha não confere.");
        }

        usuario.setSenha(hashService.getHashSenha(dto.novaSenha()));

        this.usuarioRepository.save(usuario);

        String kcId = authService.findKeycloakIdByUsername(usuario.getUsername());
        AuthResetPasswordUserDTO kcDto = new AuthResetPasswordUserDTO("password", dto.novaSenha(), false);
        authService.resetPassword(kcId, kcDto);
        // Enviar e-mail de confirmação da alteração de senha (Adicionar)

        // String linkRedefinicao = "https://redefinir-senha-teste.com";
        // int tempoExpiracao = 30;

        // ((EmailServiceImpl) emailService).sendResetPasswordEmail(
        // "kethelenvictoria2016@gmail.com",
        // usuario.getName(),
        // linkRedefinicao,
        // tempoExpiracao);

        emailService.sendPasswordChangedEmail(
                "kethelenvictoria2016@gmail.com",
                usuario.getName());
    }
}
