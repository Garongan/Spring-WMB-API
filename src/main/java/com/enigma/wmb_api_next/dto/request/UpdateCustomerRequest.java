package com.enigma.wmb_api_next.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCustomerRequest {
    @NotBlank(message = "id is required")
    private String id;
    @NotBlank(message = "name is required")
    private String name;
    private String phone;
}
