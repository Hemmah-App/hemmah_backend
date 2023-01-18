package org.help.hemah.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.help.hemah.model.embeded.BaseUserDataEntity;
import org.help.hemah.model.embeded.EntityWithUserData;
import org.help.hemah.model.enums.UserStatus;

@Entity
@Data
@NoArgsConstructor
public class Volunteer extends BaseEntity implements EntityWithUserData {

    @Embedded
    private BaseUserDataEntity baseUserDataEntity;

    @Override
    public BaseUserDataEntity getUserData() {
        return baseUserDataEntity;
    }

    public Volunteer(BaseUserDataEntity user) {
        setBaseUserDataEntity(user);
    }

}
