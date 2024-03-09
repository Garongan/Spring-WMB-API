package com.enigma.wmb_api_next.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UpdateTableMenuRequest {
    private String id;
    private String name;
}
