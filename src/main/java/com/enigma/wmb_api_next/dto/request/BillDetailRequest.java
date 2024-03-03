package com.enigma.wmb_api_next.dto.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillDetailRequest {
    private String menuId;
    private Integer qty;
    private Long price;
}
