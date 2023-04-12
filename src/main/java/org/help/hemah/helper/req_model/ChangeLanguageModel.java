package org.help.hemah.helper.req_model;

import lombok.Data;
import org.help.hemah.domain.user.UserLanguage;

@Data
public class ChangeLanguageModel {
    private UserLanguage language;
}
