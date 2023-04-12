package org.help.hemah.domain.disabled;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.help.hemah.domain.BaseEntity;
import org.help.hemah.domain.embeded.BaseUserDataEntity;
import org.help.hemah.domain.embeded.EntityWithUserData;

@Entity
@NoArgsConstructor
@Data
public class Disabled extends BaseEntity implements EntityWithUserData {

    @Embedded
    @Valid
    private BaseUserDataEntity baseUserDataEntity;

    @Override
    public BaseUserDataEntity getUserData() {
        return baseUserDataEntity;
    }

    public Disabled(BaseUserDataEntity user) {
        setBaseUserDataEntity(user);
    }

}
