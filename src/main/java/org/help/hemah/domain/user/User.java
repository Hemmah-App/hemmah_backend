package org.help.hemah.domain.user;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.help.hemah.domain.BaseEntity;
import org.help.hemah.domain.embeded.BaseUserDataEntity;
import org.help.hemah.domain.embeded.EntityWithUserData;


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

    @Enumerated(EnumType.STRING)
    private UserType type;

    @Enumerated(EnumType.STRING)
    private UserLanguage language;

    private String profilePictureUrl;

    @Override
    public BaseUserDataEntity getUserData() {
        return baseUserDataEntity;
    }

}
