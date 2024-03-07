package com.enigma.wmb_api_next.controller;

import com.enigma.wmb_api_next.constant.ApiUrl;
import com.enigma.wmb_api_next.constant.StatusMessege;
import com.enigma.wmb_api_next.dto.request.CustomerRequest;
import com.enigma.wmb_api_next.dto.response.CommonResponse;
import com.enigma.wmb_api_next.dto.response.CustomerResponse;
import com.enigma.wmb_api_next.entity.Customer;
import com.enigma.wmb_api_next.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiUrl.API_URL + ApiUrl.CUSTOMER_URL)
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<CustomerResponse>> save(@RequestBody CustomerRequest request) {
        CustomerResponse customerResponse = customerService.saveOrGet(request);
        return ResponseEntity.ok(getCommonResponse(customerResponse, HttpStatus.CREATED, StatusMessege.SUCCESS_CREATE));
    }

    @PostMapping(
            path = "/bulk",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<List<CustomerResponse>>> saveBulk(@RequestBody List<CustomerRequest> requests) {
        List<CustomerResponse> customerResponses = customerService.saveBulk(requests);
        return ResponseEntity.ok(getListCommonResponse(customerResponses, HttpStatus.CREATED, StatusMessege.SUCCESS_CREATE_LIST));
    }

    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<CustomerResponse>> getById(@PathVariable String id) {
        CustomerResponse customerResponse = customerService.getById(id);
        return ResponseEntity.ok(getCommonResponse(customerResponse, HttpStatus.OK, StatusMessege.SUCCESS_RETRIEVE));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<List<CustomerResponse>>> getAll() {
        List<CustomerResponse> customerResponses = customerService.getAll();
        return ResponseEntity.ok(getListCommonResponse(customerResponses, HttpStatus.OK, StatusMessege.SUCCESS_RETRIEVE_LIST));
    }

    @PutMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<CustomerResponse>> update(@RequestBody Customer customer) {
        CustomerResponse updated = customerService.update(customer);
        return ResponseEntity.ok(getCommonResponse(updated, HttpStatus.OK, StatusMessege.SUCCESS_UPDATE));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<CommonResponse<String>> delete(@PathVariable String id) {
        customerService.delete(id);
        CommonResponse<String> response = CommonResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message(StatusMessege.SUCCESS_DELETE)
                .data(id)
                .build();
        return ResponseEntity.ok(response);
    }

    private CommonResponse<CustomerResponse> getCommonResponse(CustomerResponse customerResponse, HttpStatus status, String message) {
        return CommonResponse.<CustomerResponse>builder()
                .statusCode(status.value())
                .message(message)
                .data(customerResponse)
                .build();
    }
    private static CommonResponse<List<CustomerResponse>> getListCommonResponse(List<CustomerResponse> customerResponses, HttpStatus status, String message) {
        return CommonResponse.<List<CustomerResponse>>builder()
                .statusCode(status.value())
                .message(message)
                .data(customerResponses)
                .build();
    }

}
