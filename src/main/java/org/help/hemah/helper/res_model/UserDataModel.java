package org.help.hemah.helper.res_model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.help.hemah.model.user.UserLanguage;
import org.help.hemah.model.user.UserType;

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
    UserType userType;
    UserLanguage language;

    Long numberOfHelpsProvided;
}
