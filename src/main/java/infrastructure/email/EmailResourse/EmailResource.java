package infrastructure.email.EmailResourse;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.reactive.ReactiveMailer;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/email")
public class EmailResource {

    @Inject
    ReactiveMailer mailer;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello";
    }

    @GET
    @Path("/mail")
    @Produces(MediaType.TEXT_PLAIN)
    public String mail() {
        Mail mail = Mail.withText(
                "kethelenvictoria2016@gmail.com",
                "Teste de envio de e-mail",
                "Este é um teste de envio de e-mail com Quarkus utilizando o Mailer.");
        mailer.send(mail).subscribe().with(
                success -> System.out.println("E-mail enviado com sucesso!"),
                failure -> System.err.println("Erro ao enviar e-mail: " + failure.getMessage()));
        return "E-mail enviado com sucesso!";

    }
}
