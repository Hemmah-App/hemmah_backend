package org.help.hemah.exeption.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Email already used.")
public class EmailUsedException extends RuntimeException{

    public EmailUsedException(String message) {
        super(message);
    }

    public EmailUsedException(String message, Throwable cause) {
        super(message, cause);
    }
}
