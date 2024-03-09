package com.enigma.wmb_api_next.service;

import com.enigma.wmb_api_next.dto.response.JwtClaims;
import com.enigma.wmb_api_next.entity.UserAccount;

public interface JwtService {
    String generateToken(UserAccount userAccount);
    boolean verifyJwtToken(String token);
    JwtClaims getClaimsByToken(String token);
}
