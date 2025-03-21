package application.email;

public interface EmailServicePort {
    void sendEmail(String to, String subject, String content);
}
