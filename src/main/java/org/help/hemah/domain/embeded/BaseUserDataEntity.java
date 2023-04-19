package org.help.hemah.domain.embeded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseUserDataEntity {

    @Pattern(regexp = "[A-Za-z][A-Za-z0-9_]{7,29}$", message = "Username is Invalid.")
    @Column(unique = true)
    private String username;

    // Stores the encoded password, The real password is validated by another class.
    private String password;

    @Email(message = "Email is Invalid.")
    @Column(unique = true)
    private String email;

    private String firstName;
    private String lastName;

    //    @Column(unique = true)
    private String phoneNumber;
    private String address;
    private Double longitude;
    private Double latitude;

    public BaseUserDataEntity(String username, String password, String email, String firstName, String lastName, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
