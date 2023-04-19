package org.help.hemah.helper.req_model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
public class HelpRequestModel {
    @Length(max = 250)
    private String title;
    @Length(max = 5000)
    private String description;

    private LocalDate date;
    private String location;
    private Double longitude;
    private Double latitude;
}
