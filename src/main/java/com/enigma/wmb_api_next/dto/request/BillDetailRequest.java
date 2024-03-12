package com.enigma.wmb_api_next.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillDetailRequest {
    @NotBlank(message = "Menu Id is required")
    private String menuId;

    @NotNull(message = "Quantity is required")
    private Integer qty;

    @NotNull(message = "Price is required")
    private Long price;
}
