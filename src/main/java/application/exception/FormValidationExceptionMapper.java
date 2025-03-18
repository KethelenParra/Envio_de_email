package application.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class FormValidationExceptionMapper implements ExceptionMapper<FormValidationException> {

    @Override
    public Response toResponse(final FormValidationException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorMessage(exception.getMessage()))
                .build();
    }

    public static class ErrorMessage {
        private String message;

        public ErrorMessage(final String message) {
            this.message = message;
        }

        public String getMessage() {
            return this.message;
        }

        public void setMessage(final String message) {
            this.message = message;
        }
    }
}
