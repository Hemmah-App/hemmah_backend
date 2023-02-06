package org.help.hemah.helper.req_model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.help.hemah.model.enums.UserType;

@Data
public class NewUserModel {


    private String userName;

    //    @ValidPassword(message = "Password is Invalid.")
    @Pattern(regexp = "[A-Za-z][A-Za-z0-9_]{7,29}$", message = "Password is Invalid.")
    private String password;
    @NotEmpty(message = "first name can not be empty.")
    private String firstName;
    @NotEmpty(message = "last name can not be empty.")
    private String lastName;
    private String phoneNumber;
    @Email(message = "Email is Invalid.")
    private String email;
    private UserType userType;
}
