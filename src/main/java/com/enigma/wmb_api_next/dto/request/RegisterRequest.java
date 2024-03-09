package com.enigma.wmb_api_next.dto.request;


import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    private String name;
    private String phone;
    private String username;
    private String password;
}
