package com.negd.viksit.bharat.audit;

import com.negd.viksit.bharat.dto.UserPrincipal;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof AnonymousAuthenticationToken) {
            return Optional.of(1l);
        } else {
            try {
                UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
                return Optional.of(principal.getUserid());
            } catch (Exception e) {
                return Optional.of(1l);
            }
        }

    }
}