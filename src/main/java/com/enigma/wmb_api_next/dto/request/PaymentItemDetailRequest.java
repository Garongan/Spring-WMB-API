package com.enigma.wmb_api_next.dto.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentItemDetailRequest {
    private String id;
    private String name;
    private Long price;
    private Integer quantity;
}
