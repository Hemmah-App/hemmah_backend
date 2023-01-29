package org.help.hemah.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.help.hemah.model.User;
import org.help.hemah.service.disabled.DisabledService;
import org.help.hemah.service.volunteer.VolunteerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {
    private final JwtEncoder encoder;
    private final JwtDecoder decoder;

    private final DisabledService disabledService;
    private final VolunteerService volunteerService;

    public String generateToken(String username) {

        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(10, ChronoUnit.HOURS))
                .subject(username)
                .build();

        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String generateToken(User user) {
        return generateToken(user.getBaseUserDataEntity().getUsername());
    }

    public Authentication getAuthentication(String token) {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        return converter.convert(decoder.decode(token));
    }

    public String getUsername(Jwt jwt) {
        return jwt.getSubject();
    }

    public Object getUser(Jwt jwt) {
        String username = getUsername(jwt);
        if (disabledService.existsByUsername(username)) {
            return disabledService.getDisabledByUsername(username);
        } else if (volunteerService.existsByUsername(username)) {
            return volunteerService.getVolunteerByUsername(username);
        }
        throw new UsernameNotFoundException("User not found");
    }

}
