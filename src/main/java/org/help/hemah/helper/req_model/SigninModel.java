package org.help.hemah.helper.req_model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SigninModel {
    @Email
    private String email;
    @NotBlank
    private String password;
}
