package com.enigma.wmb_api_next.dto.response;

import com.enigma.wmb_api_next.constant.UserRoleEnum;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtClaims {
    private String userAccountId;
    private List<String> roles;
}
