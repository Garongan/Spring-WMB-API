package com.enigma.wmb_api_next.dto.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerRequest {
    private String name;
    private String phoneNumber;
}
