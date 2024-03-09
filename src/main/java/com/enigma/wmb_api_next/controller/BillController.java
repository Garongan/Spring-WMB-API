package com.enigma.wmb_api_next.controller;

import com.enigma.wmb_api_next.constant.ApiUrl;
import com.enigma.wmb_api_next.constant.StatusMessege;
import com.enigma.wmb_api_next.dto.request.BillRequest;
import com.enigma.wmb_api_next.dto.request.SearchBillRequest;
import com.enigma.wmb_api_next.dto.response.BillResponse;
import com.enigma.wmb_api_next.dto.response.CommonResponse;
import com.enigma.wmb_api_next.service.BillService;
import com.enigma.wmb_api_next.util.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiUrl.API_URL + ApiUrl.BILL_URL)
@RequiredArgsConstructor
public class BillController {
    private final BillService billService;

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<BillResponse>> save(@RequestBody BillRequest request) {
        BillResponse bill = billService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(getCommonResponse(bill, HttpStatus.CREATED, StatusMessege.SUCCESS_CREATE));
    }


    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<BillResponse>> getById(@PathVariable String id) {
        BillResponse bill = billService.getById(id);
        return ResponseEntity.ok(getCommonResponse(bill, HttpStatus.OK, StatusMessege.SUCCESS_RETRIEVE));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<List<BillResponse>>> getAll(
            @RequestParam(name = "daily", required = false) @JsonFormat(pattern = "yyyy-MM-dd") String daily,
            @RequestParam(name = "weekly", required = false) @JsonFormat(pattern = "yyyy-MM-dd") String weeklyStart,
            @RequestParam(name = "weekly", required = false) @JsonFormat(pattern = "yyyy-MM-dd") String weeklyEnd,
            @RequestParam(name = "monthly", required = false) @JsonFormat(pattern = "yyyy-MM") String monthly
    ) {
        SearchBillRequest searchBillRequest = SearchBillRequest.builder()
                .daily(DateUtil.parseDate(daily))
                .weeklyStart(DateUtil.parseDate(weeklyStart))
                .weeklyEnd(DateUtil.parseDate(weeklyEnd))
                .monthly(DateUtil.parseDate(monthly))
                .build();
        List<BillResponse> bills = billService.getAll(searchBillRequest);
        CommonResponse<List<BillResponse>> response = CommonResponse.<List<BillResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(StatusMessege.SUCCESS_RETRIEVE_LIST)
                .data(bills)
                .build();
        return ResponseEntity.ok(response);
    }

    private static CommonResponse<BillResponse> getCommonResponse(BillResponse bill, HttpStatus status, String message) {
        return CommonResponse.<BillResponse>builder()
                .statusCode(status.value())
                .message(message)
                .data(bill)
                .build();
    }
}
