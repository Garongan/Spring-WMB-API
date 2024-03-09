package com.enigma.wmb_api_next.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerRequest {
    @NotBlank(message = "Name is required")
    private String name;

    private String phoneNumber;
}
