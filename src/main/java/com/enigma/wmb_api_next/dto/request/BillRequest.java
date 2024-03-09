package com.enigma.wmb_api_next.dto.request;

import lombok.*;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillRequest {
    private Date transDate;
    private String customerName;
    private String customerPhone;
    private String tableName;
    private String transType;
    private List<BillDetailRequest> billDetails;
}
