package infrastructure.email.EmailService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import application.email.EmailServicePort;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.reactive.ReactiveMailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class EmailServiceImpl implements EmailServicePort {
    @Inject
    ReactiveMailer mailer;

    @Override
    public void sendEmail(String to, String subject, String content) {
        Mail mail = Mail.withHtml(to, subject, content);

        mailer.send(mail)
                .subscribe()
                .with(
                        success -> System.out.println("E-mail enviado com sucesso!"),
                        failure -> System.err.println("Erro ao enviar e-mail: " + failure.getMessage()));
    }

    private String loadTemplate(String templateName) {
        try (InputStream is = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("templates/" + templateName);
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException | NullPointerException e) {
            throw new RuntimeException("Não foi possível ler o template: " + templateName, e);
        }
    }

    public void sendResetPasswordEmail(String to, String nomeUsuario, String linkRedefinicao, int tempoExpiracao) {
        String template = loadTemplate("Reset-senha.html");

        template = template
                .replace("{{nomeUsuario}}", nomeUsuario)
                .replace("{{linkRedefinicao}}", linkRedefinicao)
                .replace("{{tempoExpiracao}}", String.valueOf(tempoExpiracao));

        sendEmail(to, "Redefinição de Senha", template);
    }

    public void sendPasswordChangedEmail(String to, String nomeUsuario) {
        String template = loadTemplate("Senha-Alterada.html");

        template = template.replace("{{nomeUsuario}}", nomeUsuario);

        sendEmail(to, "Senha Alterada com Sucesso", template);
    }
}
