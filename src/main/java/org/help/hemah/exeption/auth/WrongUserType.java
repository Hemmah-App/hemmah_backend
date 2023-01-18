package org.help.hemah.exeption.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WrongUserType extends RuntimeException{

    public WrongUserType(String message) {
        super(message);
    }

    public WrongUserType(String message, Throwable cause) {
        super(message, cause);
    }
}
