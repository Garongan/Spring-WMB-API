package com.enigma.wmb_api_next.controller;

import com.enigma.wmb_api_next.constant.ApiUrl;
import com.enigma.wmb_api_next.constant.StatusMessage;
import com.enigma.wmb_api_next.dto.request.LoginRequest;
import com.enigma.wmb_api_next.dto.request.RegisterRequest;
import com.enigma.wmb_api_next.dto.response.CommonResponse;
import com.enigma.wmb_api_next.dto.response.LoginResponse;
import com.enigma.wmb_api_next.dto.response.RegisterResponse;
import com.enigma.wmb_api_next.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping(ApiUrl.API_URL + ApiUrl.AUTH_URL)
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Auth API")
public class AuthController {
        private final AuthService authService;

        @Operation(summary = "Register User")
        @SecurityRequirement(name = "Authorization")
        @PostMapping(path = "/register/user", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<CommonResponse<RegisterResponse>> registerUser(@RequestBody RegisterRequest request) {
                RegisterResponse registerResponse = authService.registerUser(request);
                CommonResponse<RegisterResponse> response = CommonResponse.<RegisterResponse>builder()
                                .statusCode(HttpStatus.CREATED.value())
                                .message(StatusMessage.SUCCESS_CREATE)
                                .data(registerResponse)
                                .build();
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        @Operation(summary = "Register Admin")
        @SecurityRequirement(name = "Authorization")
        @PreAuthorize("hasRole('SUPER_ADMIN')")
        @PostMapping(path = "/register/admin", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<CommonResponse<RegisterResponse>> registerAdmin(@RequestBody RegisterRequest request) {
                RegisterResponse registerResponse = authService.registerAdmin(request);
                CommonResponse<RegisterResponse> response = CommonResponse.<RegisterResponse>builder()
                                .statusCode(HttpStatus.CREATED.value())
                                .message(StatusMessage.SUCCESS_CREATE)
                                .data(registerResponse)
                                .build();
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        @Operation(summary = "Login")
        @SecurityRequirement(name = "Authorization")
        @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<CommonResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
                LoginResponse loginResponse = authService.login(request);
                CommonResponse<LoginResponse> response = CommonResponse.<LoginResponse>builder()
                                .statusCode(HttpStatus.OK.value())
                                .message(StatusMessage.SUCCESS_LOGIN)
                                .data(loginResponse)
                                .build();
                return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        @Operation(summary = "validate token")
        @GetMapping(path = "validate-token", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<?> validateToken(@RequestParam String token) {
                boolean isValidToken = authService.validateToken();
                if (isValidToken) {
                        CommonResponse<String> response = CommonResponse.<String>builder()
                                        .statusCode(HttpStatus.OK.value())
                                        .message(StatusMessage.SUCCESS_RETRIEVE)
                                        .build();
                        return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                        CommonResponse<String> response = CommonResponse.<String>builder()
                                        .statusCode(HttpStatus.UNAUTHORIZED.value())
                                        .message(StatusMessage.ERROR_INVALID_JWT)
                                        .build();
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
                }
        }

}
