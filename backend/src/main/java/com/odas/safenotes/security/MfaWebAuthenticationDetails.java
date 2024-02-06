package com.odas.safenotes.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

@Getter
public class MfaWebAuthenticationDetails extends WebAuthenticationDetails {

    private final String code;

    public MfaWebAuthenticationDetails(HttpServletRequest request, String code) {
        super(request);
        this.code = code;
    }
}
