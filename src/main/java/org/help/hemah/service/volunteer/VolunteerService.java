package org.help.hemah.service.volunteer;

import org.help.hemah.domain.volunteer.Volunteer;

import java.util.List;

public interface VolunteerService {
    List<Volunteer> getActiveVolunteers();
    Volunteer getVolunteerByUsername(String username);

    boolean existsByUsername(String username);

    Long getVolunteerHelpCount(String username);
}
