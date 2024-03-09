package com.enigma.wmb_api_next.dto.request;


import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateBillRequest {
    private String id;
    private String transactionStatus;
}
