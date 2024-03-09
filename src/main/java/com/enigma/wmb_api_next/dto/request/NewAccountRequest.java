package com.enigma.wmb_api_next.dto.request;

import com.enigma.wmb_api_next.entity.UserAccount;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewAccountRequest {
    private String name;
    private String phone;
    private UserAccount userAccount;
}
