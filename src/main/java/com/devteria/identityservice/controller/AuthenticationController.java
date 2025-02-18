package com.devteria.identityservice.controller;

import com.devteria.identityservice.dto.request.AuthenticationReq;
import com.devteria.identityservice.dto.response.ApiResponse;
import com.devteria.identityservice.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody AuthenticationReq authenticationReq) {
        return ResponseEntity.ok(ApiResponse.builder()
                .code(200)
                .message(authenticationService.authenticate(authenticationReq) ? "Login successful" : "Login failed")
                .build());
    }
}
