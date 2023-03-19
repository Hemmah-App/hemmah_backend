package org.help.hemah.service.disabled;

import org.help.hemah.model.disabled.Disabled;

public interface DisabledService {

    Disabled getDisabledByUsername(String username);

    boolean existsByUsername(String username);
}
