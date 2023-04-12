package org.help.hemah.service.auth;

import org.help.hemah.domain.disabled.Disabled;
import org.help.hemah.domain.user.User;
import org.help.hemah.domain.volunteer.Volunteer;

public interface AuthenticationFacade {
    User getAuthenticatedUser();

    Volunteer getAuthenticatedVolunteer();

    Disabled getAuthenticatedDisabled();
}
