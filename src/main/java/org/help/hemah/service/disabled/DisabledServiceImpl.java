package org.help.hemah.service.disabled;

import lombok.RequiredArgsConstructor;
import org.help.hemah.model.Disabled;
import org.help.hemah.repository.DisabledRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DisabledServiceImpl implements DisabledService {

    private final DisabledRepository disabledRepository;

    @Override
    public Disabled getDisabledByUsername(String username) {
        return disabledRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Disabled not found"));
    }

    @Override
    public boolean existsByUsername(String username) {
        return disabledRepository.existsByUsername(username);
    }
}
