// package service;

// import io.quarkus.mailer.Mail;
// import io.quarkus.mailer.Mailer;
// import jakarta.enterprise.context.ApplicationScoped;
// import jakarta.inject.Inject;

// @ApplicationScoped
// public class EmailService {

// @Inject
// Mailer mailer;

// public void sendEmail(String to, String subject, String body) {
// Mail email = Mail.withText(to, subject, body);
// mailer.send(email);
// System.out.println("E-mail enviado para: " + to);
// }
// }