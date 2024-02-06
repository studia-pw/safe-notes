package com.odas.safenotes.controllers;

import com.odas.safenotes.dto.user.RegisterUserRequest;
import com.odas.safenotes.dto.user.TotpQrRequest;
import com.odas.safenotes.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterUserRequest registerUserRequest) {
        userService.register(registerUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/2fa")
    public ResponseEntity<Map<String, String>> twoFactorAuth(@RequestBody @Valid TotpQrRequest totpQrRequest) {
        HashMap<String, String> response = new HashMap<>();
        response.put("qrUrl", userService.twoFactorAuth(totpQrRequest.email()));

        return ResponseEntity.ok(response);
    }

}
