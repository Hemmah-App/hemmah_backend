package org.help.hemah.helper.req_model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NewMessageModel {

    Long chatId;
    Long helpRequestId;
    @NotBlank
    String content;
}
