package org.help.hemah.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.help.hemah.model.embeded.BaseUserDataEntity;
import org.help.hemah.model.embeded.EntityWithUserData;

@Entity
@NoArgsConstructor
@Data
public class Disabled extends BaseEntity implements EntityWithUserData {

    @Embedded
    private BaseUserDataEntity baseUserDataEntity;

    @Override
    public BaseUserDataEntity getUserData() {
        return baseUserDataEntity;
    }

    public Disabled(BaseUserDataEntity user) {
        setBaseUserDataEntity(user);
    }

}
