package com.fpl.datn.controller;

import java.text.ParseException;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.*;

import com.fpl.datn.dto.ApiResponse;
import com.fpl.datn.dto.request.AuthenticationRequest;
import com.fpl.datn.dto.request.IntrospectRequest;
import com.fpl.datn.dto.request.LogoutRequest;
import com.fpl.datn.dto.request.RefreshRequest;
import com.fpl.datn.dto.response.AuthenticationResponse;
import com.fpl.datn.dto.response.IntrospectResponse;
import com.fpl.datn.service.AuthenticationService;
import com.fpl.datn.service.CartService;
import com.nimbusds.jose.JOSEException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;
    CartService cartService;

    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> getToken(@RequestBody AuthenticationRequest request, HttpSession session) {
        var response = authenticationService.authenticated(request);

        cartService.mergeSessionCartToUser(session);
        return ApiResponse.<AuthenticationResponse>builder()
                .code(1000)
                .result(response)
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        return ApiResponse.<IntrospectResponse>builder()
                .code(1000)
                .result(authenticationService.introspectResponse(request))
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder().code(1000).build();
    }

    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> refresh(@RequestBody RefreshRequest request)
            throws ParseException, JOSEException {
        return ApiResponse.<AuthenticationResponse>builder()
                .code(1000)
                .result(authenticationService.refreshToken(request))
                .build();
    }
}
