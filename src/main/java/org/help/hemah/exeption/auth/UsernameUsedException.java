package org.help.hemah.exeption.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "username already used.")
public class UsernameUsedException extends RuntimeException {
    public UsernameUsedException(String message) {
        super(message);
    }

    public UsernameUsedException(String message, Throwable cause) {
        super(message, cause);
    }
}
