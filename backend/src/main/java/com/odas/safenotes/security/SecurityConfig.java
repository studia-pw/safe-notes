package com.odas.safenotes.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.odas.safenotes.domain.LoginAudit;
import com.odas.safenotes.domain.User;
import com.odas.safenotes.mappers.LoginAuditService;
import com.odas.safenotes.mappers.UserMapper;
import com.odas.safenotes.repositories.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final long UNSUCCESSFUL_LOGIN_DELAY = 2000;
    private final UserMapper userMapper;
    private final LoginAuditService loginAuditService;
    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors((cors) -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .headers((headers) -> {
                    headers.contentSecurityPolicy((content) -> content.policyDirectives("default-src 'self'"));
                })
                .formLogin((formLogin) -> formLogin.loginProcessingUrl("/api/auth/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler(successHandler())
                        .failureHandler(failureHandler())
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutSuccessHandler(logoutSuccessHandler())
                        .logoutUrl("/api/auth/logout")
                        .permitAll())
                .authorizeHttpRequests((request) -> {
                    request
                            .requestMatchers("/api/auth/**").permitAll()
                            .anyRequest().authenticated();
                })
                .exceptionHandling((handler) -> {
                    handler.authenticationEntryPoint((httpServletRequest, httpServletResponse, e) -> {
                        httpServletResponse.setStatus(401);
                    });
                })
        ;

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:4200");
        configuration.setAllowedMethods(List.of("GET", "POST"));
        configuration.setAllowedHeaders(List.of("Content-Type", "Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private AuthenticationSuccessHandler successHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                                HttpServletResponse httpServletResponse, Authentication authentication)
                    throws IOException, ServletException {
                User user = (User) authentication.getPrincipal();
                final var userResource = userMapper.fromUser(user);
                final var loginAudit = LoginAudit.builder()
                        .id(null)
                        .user(user)
                        .ip(httpServletRequest.getRemoteAddr())
                        .success(true)
                        .build();
                loginAuditService.saveLoginAudit(loginAudit);
                System.out.println("Success");
                httpServletResponse.getWriter().write(new ObjectMapper().writeValueAsString(userResource));
                httpServletResponse.setStatus(200);
            }
        };
    }

    private AuthenticationFailureHandler failureHandler() {
        return new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest httpServletRequest,
                                                HttpServletResponse httpServletResponse, AuthenticationException e)
                    throws IOException, ServletException {
                System.out.println("Failure");
                try {
                    Thread.sleep(UNSUCCESSFUL_LOGIN_DELAY);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }

                System.out.println(httpServletRequest.getParameter("email"));
                final var email = httpServletRequest.getParameter("email");
                final var user = userRepository.findByEmailIgnoreCase(email);
                final var loginAudit = LoginAudit.builder()
                        .id(null)
                        .user(user.orElse(null))
                        .ip(httpServletRequest.getRemoteAddr())
                        .success(false)
                        .build();
                loginAuditService.saveLoginAudit(loginAudit);
                httpServletResponse.getWriter().append("Authentication failure");
                httpServletResponse.setStatus(401);
            }
        };
    }

    private LogoutSuccessHandler logoutSuccessHandler() {
        return new LogoutSuccessHandler() {
            @Override
            public void onLogoutSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse, Authentication authentication)
                    throws IOException, ServletException {
                System.out.println("Logout");
                httpServletResponse.setStatus(200);
            }
        };

    }
}
