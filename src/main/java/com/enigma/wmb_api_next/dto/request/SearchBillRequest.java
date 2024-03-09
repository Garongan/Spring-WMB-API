package com.enigma.wmb_api_next.dto.request;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchBillRequest {
    private Date daily;
    private Date weeklyStart;
    private Date weeklyEnd;
    private Date monthly;
    private String direction;
    private String sortBy;
    private Integer page;
    private Integer size;
}
