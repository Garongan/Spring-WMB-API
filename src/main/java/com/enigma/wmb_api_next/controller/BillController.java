package com.enigma.wmb_api_next.controller;

import com.enigma.wmb_api_next.constant.ApiUrl;
import com.enigma.wmb_api_next.constant.StatusMessege;
import com.enigma.wmb_api_next.dto.request.BillRequest;
import com.enigma.wmb_api_next.dto.request.SearchBillRequest;
import com.enigma.wmb_api_next.dto.request.UpdateBillRequest;
import com.enigma.wmb_api_next.dto.response.BillResponse;
import com.enigma.wmb_api_next.dto.response.CommonResponse;
import com.enigma.wmb_api_next.dto.response.PaginationResponse;
import com.enigma.wmb_api_next.service.BillService;
import com.enigma.wmb_api_next.service.Impl.PdfServiceImpl;
import com.enigma.wmb_api_next.service.PdfService;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lowagie.text.DocumentException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(ApiUrl.API_URL + ApiUrl.BILL_URL)
@RequiredArgsConstructor
public class BillController {
    private final BillService billService;
    private List<BillResponse> billResponseList;

    @Operation(summary = "Save new bill")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'USER') or authenticated")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<?>> save(@RequestBody BillRequest request) {
        BillResponse bill = billService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(getCommonResponse(bill, HttpStatus.CREATED, StatusMessege.SUCCESS_CREATE));
    }

    @Operation(summary = "Get bill by id")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<BillResponse>> getById(@PathVariable String id) {
        BillResponse bill = billService.getById(id);
        return ResponseEntity.ok(getCommonResponse(bill, HttpStatus.OK, StatusMessege.SUCCESS_RETRIEVE));
    }

    @Operation(
            summary = "Get all bill",
            description = "Get all bill with pagination, sort, and search by date, and filter by daily, weekly, and monthly."
    )
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<List<BillResponse>>> getAll(
            @RequestParam(name = "daily", required = false) @JsonFormat(pattern = "yyyy-MM-dd") String daily,
            @RequestParam(name = "weekStart", required = false) @JsonFormat(pattern = "yyyy-MM-dd") String weeklyStart,
            @RequestParam(name = "weekEnd", required = false) @JsonFormat(pattern = "yyyy-MM-dd") String weeklyEnd,
            @RequestParam(name = "monthly", required = false) @JsonFormat(pattern = "yyyy-MM") String monthly,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "sortBy", defaultValue = "transDate") String sortBy,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        SearchBillRequest searchBillRequest = SearchBillRequest.builder()
                .daily(daily)
                .weeklyStart(weeklyStart)
                .weeklyEnd(weeklyEnd)
                .monthly(monthly)
                .direction(direction)
                .sortBy(sortBy)
                .page(page)
                .size(size)
                .build();
        Page<BillResponse> bills = billService.getAll(searchBillRequest);

        this.billResponseList = bills.getContent();

        PaginationResponse paginationResponse = PaginationResponse.builder()
                .totalPages(bills.getTotalPages())
                .totalElement(bills.getTotalElements())
                .page(bills.getNumber() + 1)
                .size(bills.getSize())
                .hasNext(bills.hasNext())
                .hasPrevious(bills.hasPrevious())
                .build();

        CommonResponse<List<BillResponse>> response = CommonResponse.<List<BillResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(StatusMessege.SUCCESS_RETRIEVE_LIST)
                .data(bills.getContent())
                .paginationResponse(paginationResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Export bill to pdf")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @GetMapping(path = "/export/pdf")
    public void exportBillPdf(HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        String headerValue = "attachment; filename=bills.pdf";
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, headerValue);
        PdfService pdfService = new PdfServiceImpl(billResponseList);
        pdfService.export(response);
    }

    @Operation(summary = "Update status payment")
    @PostMapping(
            path = "/status",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<?>> updateStatus(@RequestBody Map<String, Object> request) {
        UpdateBillRequest updateBillRequest = UpdateBillRequest.builder()
                .id(request.get("order_id").toString())
                .transactionStatus(request.get("transaction_status").toString())
                .build();
        billService.updateStatusPayment(updateBillRequest);
        return ResponseEntity.ok(getCommonResponse(null, HttpStatus.OK, StatusMessege.SUCCESS_UPDATE));
    }

    private static CommonResponse<BillResponse> getCommonResponse(BillResponse bill, HttpStatus status, String message) {
        return CommonResponse.<BillResponse>builder()
                .statusCode(status.value())
                .message(message)
                .data(bill)
                .build();
    }
}
