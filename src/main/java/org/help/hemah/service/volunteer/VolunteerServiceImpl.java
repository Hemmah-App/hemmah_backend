package org.help.hemah.service.volunteer;

import org.help.hemah.model.enums.UserStatus;
import org.help.hemah.model.Volunteer;
import org.help.hemah.repository.VolunteerRepository;

import java.util.List;

public class VolunteerServiceImpl implements VolunteerService {

    private final VolunteerRepository volunteerRepository;

    public VolunteerServiceImpl(VolunteerRepository volunteerRepository) {
        this.volunteerRepository = volunteerRepository;
    }

    @Override
    public List<Volunteer> getActiveVolunteers() {
        return volunteerRepository.findAllByStatus(UserStatus.ACTIVE);
    }

}
