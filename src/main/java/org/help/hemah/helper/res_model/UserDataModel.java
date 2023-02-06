package org.help.hemah.helper.res_model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.help.hemah.model.enums.UserType;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDataModel {
    String username;
    String email;
    String firstName;
    String lastName;
    String phoneNumber;
    String address;
    String city;
    UserType userType;

}
