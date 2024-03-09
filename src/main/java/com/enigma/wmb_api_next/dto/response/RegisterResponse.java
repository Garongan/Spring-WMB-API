package com.enigma.wmb_api_next.dto.response;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterResponse {
    private String username;
    private List<String> roles;
}
