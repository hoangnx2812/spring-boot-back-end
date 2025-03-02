package com.devteria.identityservice.controller;

import com.devteria.identityservice.dto.request.AuthenticationReq;
import com.devteria.identityservice.dto.request.IntrospectReq;
import com.devteria.identityservice.dto.request.LogoutReq;
import com.devteria.identityservice.dto.request.RefreshTokenReq;
import com.devteria.identityservice.dto.response.ApiResponse;
import com.devteria.identityservice.dto.response.AuthenticationResponse;
import com.devteria.identityservice.dto.response.IntrospectResponse;
import com.devteria.identityservice.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationReq authenticationReq) {
        return ApiResponse.<AuthenticationResponse>builder()
                .code(200)
                .message("Login successful")
                .data(authenticationService.authenticate(authenticationReq))
                .build();
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectReq introspectReq)
            throws ParseException, JOSEException {
        return ApiResponse.<IntrospectResponse>builder()
                .code(200)
                .message("Introspect successful")
                .data(authenticationService.introspect(introspectReq))
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody LogoutReq logoutReq) throws ParseException, JOSEException {
        authenticationService.logout(logoutReq);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Logout successful")
                .build();
    }


    @PostMapping("/refresh")
    public ApiResponse<AuthenticationResponse> refresh(@RequestBody RefreshTokenReq refreshTokenReq) throws ParseException, JOSEException {
        return ApiResponse.<AuthenticationResponse>builder()
                .code(200)
                .message("Refresh successful")
                .data(authenticationService.refreshToken(refreshTokenReq))
                .build();
    }


}
