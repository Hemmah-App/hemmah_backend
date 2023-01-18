package org.help.hemah.service.volunteer;

import org.help.hemah.model.Volunteer;

import java.util.List;

public interface VolunteerService {
    List<Volunteer> getActiveVolunteers();
}
