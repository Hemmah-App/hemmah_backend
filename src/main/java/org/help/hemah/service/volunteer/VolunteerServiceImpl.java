package org.help.hemah.service.volunteer;

import org.help.hemah.model.enums.UserStatus;
import org.help.hemah.model.Volunteer;
import org.help.hemah.repository.VolunteerRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class VolunteerServiceImpl implements VolunteerService {

    private final VolunteerRepository volunteerRepository;

    public VolunteerServiceImpl(VolunteerRepository volunteerRepository) {
        this.volunteerRepository = volunteerRepository;
    }

    @Override
    public List<Volunteer> getActiveVolunteers() {
        return volunteerRepository.findAllByStatus(UserStatus.ACTIVE);
    }

    @Override
    public Volunteer getVolunteerByUsername(String username) {
        return volunteerRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("Volunteer not found"));
    }

    @Override
    public boolean existsByUsername(String username) {
        return volunteerRepository.existsByUsername(username);
    }

}
