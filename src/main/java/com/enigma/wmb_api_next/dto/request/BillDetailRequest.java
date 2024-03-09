package com.enigma.wmb_api_next.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillDetailRequest {
    @NotBlank(message = "Menu Id is required")
    private String menuId;

    @NotBlank(message = "Quantity is required")
    private Integer qty;

    @NotBlank(message = "Price is required")
    private Long price;
}
