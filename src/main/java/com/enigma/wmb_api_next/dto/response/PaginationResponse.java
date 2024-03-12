package com.enigma.wmb_api_next.dto.response;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaginationResponse {
    private Integer totalPages;
    private Long totalElement;
    private Integer page;
    private Integer size;
    private Boolean hasNext;
    private Boolean hasPrevious;
}
