package com.odas.safenotes.security;

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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfigurationSource;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserMapper userMapper;
    private final LoginAuditService loginAuditService;
    private final UserRepository userRepository;
    private final CorsConfigurationSource corsConfigurationSource;
    private final AuthenticationSuccessHandler successHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors((cors) -> cors.configurationSource(corsConfigurationSource))
                .csrf(AbstractHttpConfigurer::disable)
                .headers((headers) -> {
                    headers.contentSecurityPolicy((content) -> content.policyDirectives("default-src 'self'"));
                })
                .formLogin((formLogin) -> formLogin.loginProcessingUrl("/api/auth/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler(successHandler)
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
