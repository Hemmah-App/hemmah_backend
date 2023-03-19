package org.help.hemah.helper.req_model;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ChangePasswordModel {

    private String oldPassword;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$", message = """
            The Password Must Contain at least
             - 1 Upper Case Character
             - 1 Numeric Value
             - 1 Special Character .""")
    private String newPassword;
}
