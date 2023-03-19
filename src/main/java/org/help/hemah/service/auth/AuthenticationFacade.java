package org.help.hemah.service.auth;

import org.help.hemah.model.disabled.Disabled;
import org.help.hemah.model.user.User;
import org.help.hemah.model.volunteer.Volunteer;

public interface AuthenticationFacade {
    User getAuthenticatedUser();

    Volunteer getAuthenticatedVolunteer();

    Disabled getAuthenticatedDisabled();
}
