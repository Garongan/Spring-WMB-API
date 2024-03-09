package com.enigma.wmb_api_next.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDetailRequest {
    @JsonProperty("order_id")
    private String id;
    @JsonProperty("gross_amount")
    private Long amount;
}
