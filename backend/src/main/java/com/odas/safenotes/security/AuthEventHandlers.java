package com.odas.safenotes.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.odas.safenotes.domain.User;
import com.odas.safenotes.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@RequiredArgsConstructor
public class AuthEventHandlers {

    private final long UNSUCCESSFUL_LOGIN_DELAY = 2000;
    private final UserMapper userMapper;

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            final var user = userMapper.fromUser((User) authentication.getPrincipal());
            response.getWriter().write(new ObjectMapper().writeValueAsString(user));
            response.setStatus(200);
        };
    }


//    private AuthenticationFailureHandler failureHandler() {
//        return new AuthenticationFailureHandler() {
//            @Override
//            public void onAuthenticationFailure(HttpServletRequest httpServletRequest,
//                                                HttpServletResponse httpServletResponse, AuthenticationException e)
//                    throws IOException, ServletException {
//                System.out.println("Failure");
//                try {
//                    Thread.sleep(UNSUCCESSFUL_LOGIN_DELAY);
//                } catch (InterruptedException ex) {
//                    throw new RuntimeException(ex);
//                }
//
//                System.out.println(httpServletRequest.getParameter("email"));
//                final var email = httpServletRequest.getParameter("email");
//                final var user = userRepository.findByEmailIgnoreCase(email);
//                final var loginAudit = LoginAudit.builder()
//                        .id(null)
//                        .user(user.orElse(null))
//                        .ip(httpServletRequest.getRemoteAddr())
//                        .success(false)
//                        .build();
//                loginAuditService.saveLoginAudit(loginAudit);
//                httpServletResponse.getWriter().append("Authentication failure");
//                httpServletResponse.setStatus(401);
//            }
//        };
//    }
}
