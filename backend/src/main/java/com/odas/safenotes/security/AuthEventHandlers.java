package com.odas.safenotes.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.odas.safenotes.domain.User;
import com.odas.safenotes.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@RequiredArgsConstructor
public class AuthEventHandlers {

    private final long LOGIN_DELAY = 2000;
    private final UserMapper userMapper;

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            final var user = userMapper.fromUser((User) authentication.getPrincipal());

            try {
                Thread.sleep(LOGIN_DELAY);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }

            response.getWriter().write(new ObjectMapper().writeValueAsString(user));
            response.setStatus(200);
        };
    }


    @Bean
    public AuthenticationFailureHandler failureHandler() {
        return (request, response, exception) -> {
            try {
                Thread.sleep(LOGIN_DELAY);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }

            response.getWriter().append("Authentication failure");
            response.setStatus(401);
        };
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return (httpServletRequest, httpServletResponse, authentication) -> {
            httpServletResponse.setStatus(200);
        };
    }
}
