package com.enigma.wmb_api_next.dto.response;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillDetailResponse {
    private String id;
    private String menuId;
    private Integer qty;
    private Long price;
}
