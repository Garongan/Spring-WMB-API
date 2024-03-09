package com.enigma.wmb_api_next.dto.request;

import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Customer name is required")
    private String customerName;

    private String customerPhone;

    private String tableName;

    @NotBlank(message = "Transaction type is required")
    private String transType;

    @NotBlank(message = "Bill Detail is required")
    private List<BillDetailRequest> billDetails;
}
