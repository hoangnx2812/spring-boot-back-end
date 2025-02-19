package com.devteria.identityservice.controller;

import com.devteria.identityservice.dto.request.AuthenticationReq;
import com.devteria.identityservice.dto.request.IntrospectReq;
import com.devteria.identityservice.dto.response.ApiResponse;
import com.devteria.identityservice.dto.response.IntrospectResponse;
import com.devteria.identityservice.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

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
                .message("Login successful")
                .data(authenticationService.authenticate(authenticationReq))
                .build());
    }

    @PostMapping("/introspect")
    public ResponseEntity<ApiResponse<IntrospectResponse>> introspect(@RequestBody IntrospectReq introspectReq)
            throws ParseException, JOSEException {
        return ResponseEntity.ok(ApiResponse.<IntrospectResponse>builder()
                .code(200)
                .message("Introspect successful")
                .data(authenticationService.introspect(introspectReq))
                .build());
    }
}
