package com.enigma.wmb_api_next.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuRequest {
    @NotBlank(message = "Menu Name is required")
    private String name;

    @NotNull(message = "Menu Price is required")
    private Long price;

    private MultipartFile image;
}
