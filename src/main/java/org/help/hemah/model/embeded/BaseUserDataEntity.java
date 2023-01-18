package org.help.hemah.model.embeded;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.help.hemah.model.enums.UserStatus;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseUserDataEntity {

//    @Pattern(regexp = "[A-Za-z][A-Za-z0-9_]{7,29}$", message = "Username is Invalid.")
    @NotBlank(message = "cannot be empty.")
    @Column(unique = true)
    private String username;

//    @Pattern(regexp = "[A-Za-z][A-Za-z0-9_]{7,29}$")
    @NotEmpty
    private String password;

    @Email(message = "Email is Invalid.")
    @NotEmpty
    private String email;

    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;

}
