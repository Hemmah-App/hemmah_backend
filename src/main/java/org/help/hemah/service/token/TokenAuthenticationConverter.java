package org.help.hemah.service.token;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class TokenAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final String ROLE_AUTHORITY_PREFIX = "ROLE_";

    private static final Collection<String> WELL_KNOWN_ROLE_ATTRIBUTE_NAMES = Arrays.asList("scope", "scp", "authorities", "roles");

    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        Collection<SimpleGrantedAuthority> authorities = this.getAuthorities(source);

        return new JwtAuthenticationToken(source, authorities);
    }


    private Collection<SimpleGrantedAuthority> getAuthorities(Jwt jwt) {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (String attributeName : WELL_KNOWN_ROLE_ATTRIBUTE_NAMES) {
            Object roles = jwt.getClaims().get(attributeName);
            if (StringUtils.hasText((String) roles)) {
                authorities.addAll(
                        Arrays.stream(((String) roles).split(","))
                                .map(a -> new SimpleGrantedAuthority(ROLE_AUTHORITY_PREFIX + a))
                                .toList());
            }
        }
        return authorities;
    }
}
