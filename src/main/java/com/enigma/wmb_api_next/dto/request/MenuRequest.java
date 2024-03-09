package com.enigma.wmb_api_next.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuRequest {
    @NotBlank(message = "Menu Name is required")
    private String name;

    @NotBlank(message = "Menu Price is required")
    private Long price;
}
