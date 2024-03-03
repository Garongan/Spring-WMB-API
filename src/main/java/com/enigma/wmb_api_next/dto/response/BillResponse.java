package com.enigma.wmb_api_next.dto.response;

import lombok.*;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillResponse {
    private String id;
    private Date transDate;
    private String customerId;
    private String customerName;
    private String tableName;
    private String transType;
    private List<BillDetailResponse> billdetails;
}
