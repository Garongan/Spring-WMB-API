package com.enigma.wmb_api_next.dto.response;


import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerResponse {
    private String name;
    private String phoneNumber;
}
