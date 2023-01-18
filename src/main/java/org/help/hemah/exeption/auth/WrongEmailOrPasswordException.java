package org.help.hemah.exeption.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WrongEmailOrPasswordException extends RuntimeException{
    public WrongEmailOrPasswordException(String message) {
        super(message);
    }

    public WrongEmailOrPasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}
