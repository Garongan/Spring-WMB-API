package com.enigma.wmb_api_next.service;

import com.enigma.wmb_api_next.dto.request.LoginRequest;
import com.enigma.wmb_api_next.dto.request.RegisterRequest;
import com.enigma.wmb_api_next.dto.response.LoginResponse;
import com.enigma.wmb_api_next.dto.response.RegisterResponse;

public interface AuthService {
    RegisterResponse registerUser(RegisterRequest request);
    RegisterResponse registerAdmin(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}
