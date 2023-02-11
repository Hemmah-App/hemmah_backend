package org.help.hemah.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.help.hemah.model.embeded.BaseUserDataEntity;
import org.help.hemah.model.embeded.EntityWithUserData;

@Entity
@Data
@NoArgsConstructor
public class Volunteer extends BaseEntity implements EntityWithUserData {

    @Embedded
    @Valid
    private BaseUserDataEntity baseUserDataEntity;

    @Override
    public BaseUserDataEntity getUserData() {
        return baseUserDataEntity;
    }

    public Volunteer(BaseUserDataEntity user) {
        setBaseUserDataEntity(user);
    }

}
