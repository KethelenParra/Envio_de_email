package application.exception;

import java.io.Serial;

public class FormValidationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -1897482194511192996L;

    public FormValidationException(final String message) {
        super(message);
    }
}
