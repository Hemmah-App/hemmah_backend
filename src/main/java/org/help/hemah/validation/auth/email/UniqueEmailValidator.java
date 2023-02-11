package org.help.hemah.validation.auth.email;

import jakarta.validation.ConstraintValidator;
import org.help.hemah.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Autowired
    UserRepository userRepository;

    @Override
    public boolean isValid(String value, jakarta.validation.ConstraintValidatorContext context) {
        return !userRepository.existsByEmail(value);
    }
}
