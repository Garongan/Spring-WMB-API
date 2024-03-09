package com.enigma.wmb_api_next.service.Impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.enigma.wmb_api_next.constant.StatusMessage;
import com.enigma.wmb_api_next.dto.response.JwtClaims;
import com.enigma.wmb_api_next.entity.UserAccount;
import com.enigma.wmb_api_next.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {
    private final String JWT_SECRET;
    private final String JWT_ISSUER;
    private final long JWT_EXPIRATION;

    public JwtServiceImpl(@Value("${wmb_api_next.jwt.secret}") String JWT_SECRET,
                          @Value("${wmb_api_next.jwt.issuer}") String JWT_ISSUER,
                          @Value("${wmb_api_next.jwt.expiration}") long JWT_EXPIRATION) {
        this.JWT_SECRET = JWT_SECRET;
        this.JWT_ISSUER = JWT_ISSUER;
        this.JWT_EXPIRATION = JWT_EXPIRATION;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String generateToken(UserAccount userAccount) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(JWT_SECRET);
            return JWT.create()
                    .withSubject(userAccount.getId())
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(Instant.now().plusSeconds(JWT_EXPIRATION))
                    .withClaim("roles", userAccount.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                    .withIssuer(JWT_ISSUER)
                    .sign(algorithm);
        }catch (JWTCreationException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, StatusMessage.ERROR_CREATING_JWT);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean verifyJwtToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(JWT_SECRET);
            algorithm.verify(JWT.require(algorithm).build().verify(parseToken(token)));
            return true;
        } catch (JWTCreationException e) {
            log.error("Invalid JWT Signature/Claims : {}", e.getMessage());
            return false;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JwtClaims getClaimsByToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(JWT_SECRET);
            DecodedJWT decodedJWT = JWT.require(algorithm).withIssuer(JWT_ISSUER).build().verify(parseToken(token));
            return JwtClaims.builder()
                    .userAccountId(decodedJWT.getSubject())
                    .roles(decodedJWT.getClaim("roles").asList(String.class))
                    .build();
        } catch (JWTCreationException e){
            log.error("Invalid JWT Signature/Claims : {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, StatusMessage.ERROR_CREATING_JWT);
        }
    }

    private String parseToken(String bearerToken) {
        return bearerToken.replace("Bearer ", "");
    }
}
