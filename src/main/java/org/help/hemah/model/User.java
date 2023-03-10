package org.help.hemah.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.help.hemah.model.embeded.BaseUserDataEntity;
import org.help.hemah.model.embeded.EntityWithUserData;
import org.help.hemah.model.enums.UserStatus;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity implements EntityWithUserData {

    @Embedded
    @Valid
    private BaseUserDataEntity baseUserDataEntity;

    @Column(columnDefinition = "boolean DEFAULT true")
    private boolean enabled = true;

    private String roles;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Override
    public BaseUserDataEntity getUserData() {
        return baseUserDataEntity;
    }

}
