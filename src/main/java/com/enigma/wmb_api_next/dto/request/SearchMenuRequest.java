package com.enigma.wmb_api_next.dto.request;


import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchMenuRequest {
    private String name;
    private Long minPrice;
    private Long maxPrice;
    private String direction;
    private String sortBy;
    private Integer page;
    private Integer size;
}
