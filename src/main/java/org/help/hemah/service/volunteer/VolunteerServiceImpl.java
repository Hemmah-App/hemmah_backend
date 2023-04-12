package org.help.hemah.service.volunteer;

import org.help.hemah.domain.user.UserStatus;
import org.help.hemah.domain.volunteer.Volunteer;
import org.help.hemah.repository.VolunteerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VolunteerServiceImpl implements VolunteerService {

    private final VolunteerRepository volunteerRepository;

    @Autowired
    public VolunteerServiceImpl(VolunteerRepository volunteerRepository) {
        this.volunteerRepository = volunteerRepository;
    }

    @Override
    public Volunteer getVolunteerByUsername(String username) {
        return volunteerRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Volunteer not found"));
    }

    @Override
    public boolean existsByUsername(String username) {
        return volunteerRepository.existsByUsername(username);
    }

    @Override
    public List<Volunteer> getActiveVolunteers() {
        return volunteerRepository.findAllByStatus(UserStatus.ACTIVE);
    }

    @Override
    public Long getVolunteerHelpCount(String username) {
        Long helpVideoCallsCount = volunteerRepository.countVolunteerHelpVideoCallsByUsername(username);
        // Think About Adding other types of help here before returning
        return helpVideoCallsCount;
    }
}
