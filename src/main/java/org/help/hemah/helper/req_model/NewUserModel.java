package org.help.hemah.helper.req_model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.help.hemah.domain.user.UserType;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@SuperBuilder
public class NewUserModel {

    @Pattern(regexp = "[A-Za-z][A-Za-z0-9_]{7,29}$", message = "Username is Invalid.")
    private String userName;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$", message = """
            The Password Must Contain at least
             - 1 Upper Case Character
             - 1 Numeric Value
             - 1 Special Character .""")
    private String password;

    @NotEmpty(message = "first name can not be empty.")
    private String firstName;

    @NotEmpty(message = "last name can not be empty.")
    private String lastName;

    // Optional Parameters
    private String address;
    private Double longitude;
    private Double latitude;


    private String phoneNumber;

    @Email(message = "Email is Invalid.")
    private String email;

    private UserType userType;
}
