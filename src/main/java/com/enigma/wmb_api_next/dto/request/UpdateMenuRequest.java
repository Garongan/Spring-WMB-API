package com.enigma.wmb_api_next.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateMenuRequest {
    private String id;
    private String name;
    private Long price;
    private MultipartFile image;
}
