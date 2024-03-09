package com.enigma.wmb_api_next.dto.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchCustomerRequest {
    private String name;
    private String direction;
    private String sortBy;
    private Integer page;
    private Integer size;
}
