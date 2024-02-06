package com.odas.safenotes.security;

import com.odas.safenotes.domain.User;
import com.odas.safenotes.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jboss.aerogear.security.otp.Totp;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@RequiredArgsConstructor
public class MfaAuthenticationProvider extends DaoAuthenticationProvider {

    private final UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        final MfaWebAuthenticationDetails details = (MfaWebAuthenticationDetails) auth.getDetails();
        final String code = details.getCode();
        final User user = userRepository.findByEmailIgnoreCase(auth.getName())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        final Totp totp = new Totp(user.getSecret());
        if (!(isValidLong(code) && totp.verify(code))) {
            throw new BadCredentialsException("Invalid code");
        }

        return super.authenticate(auth);
    }

    private boolean isValidLong(String code) {
        try {
            Long.parseLong(code);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
