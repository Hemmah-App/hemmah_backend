package org.help.hemah.service.disabled;

import org.help.hemah.domain.disabled.Disabled;

public interface DisabledService {

    Disabled getDisabledByUsername(String username);

    boolean existsByUsername(String username);
}
