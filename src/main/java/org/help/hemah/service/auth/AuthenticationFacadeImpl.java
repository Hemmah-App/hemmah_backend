package org.help.hemah.service.auth;

import lombok.RequiredArgsConstructor;
import org.help.hemah.domain.disabled.Disabled;
import org.help.hemah.domain.user.User;
import org.help.hemah.domain.volunteer.Volunteer;
import org.help.hemah.repository.DisabledRepository;
import org.help.hemah.repository.UserRepository;
import org.help.hemah.repository.VolunteerRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationFacadeImpl implements AuthenticationFacade {

    private final UserRepository userRepository;
    private final VolunteerRepository volunteerRepository;
    private final DisabledRepository disabledRepository;

    @Override
    public User getAuthenticatedUser() {
        return userRepository.findByUsername(
                ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getSubject()
        ).orElseThrow();
    }

    @Override
    public Volunteer getAuthenticatedVolunteer() {
        return volunteerRepository.findByUsername(
                ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getSubject()
        ).orElseThrow(() -> new RuntimeException("Volunteer not found"));
    }

    @Override
    public Disabled getAuthenticatedDisabled() {
        return disabledRepository.findByUsername(
                ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getSubject()
        ).orElseThrow(() -> new RuntimeException("Disabled not found"));
    }
}
