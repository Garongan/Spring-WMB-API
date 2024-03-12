package com.enigma.wmb_api_next.controller;

import com.enigma.wmb_api_next.constant.ApiUrl;
import com.enigma.wmb_api_next.dto.request.CustomerRequest;
import com.enigma.wmb_api_next.dto.request.SearchCustomerRequest;
import com.enigma.wmb_api_next.dto.request.UpdateCustomerRequest;
import com.enigma.wmb_api_next.dto.response.CommonResponse;
import com.enigma.wmb_api_next.dto.response.CustomerResponse;
import com.enigma.wmb_api_next.dto.response.PaginationResponse;
import com.enigma.wmb_api_next.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
        CommonResponse<CustomerResponse> response = CommonResponse.<CustomerResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Customer successfully created")
                .data(customerResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping(
            path = "/bulk",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<List<CustomerResponse>>> saveBulk(@RequestBody List<CustomerRequest> requests) {
        List<CustomerResponse> customerResponses = customerService.saveBulk(requests);
        CommonResponse<List<CustomerResponse>> responses = CommonResponse.<List<CustomerResponse>>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("List of Customer successfully created")
                .data(customerResponses)
                .build();
        return ResponseEntity.ok(responses);
    }

    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<CustomerResponse>> getById(@PathVariable String id) {
        CustomerResponse customerResponse = customerService.getById(id);
        CommonResponse<CustomerResponse> response = CommonResponse.<CustomerResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Customer successfully retrieved")
                .data(customerResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<List<CustomerResponse>>> getAll(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "direction", defaultValue = "asc") String direction,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        SearchCustomerRequest request = SearchCustomerRequest.builder()
                .name(name)
                .direction(direction)
                .sortBy(sortBy)
                .page(page)
                .size(size)
                .build();
        Page<CustomerResponse> customerResponses = customerService.getAll(request);

        PaginationResponse paginationResponse = PaginationResponse.builder()
                .totalPages(customerResponses.getTotalPages())
                .totalElement(customerResponses.getTotalElements())
                .page(customerResponses.getNumber() + 1)
                .size(customerResponses.getSize())
                .hasNext(customerResponses.hasNext())
                .hasPrevious(customerResponses.hasPrevious())
                .build();

        CommonResponse<List<CustomerResponse>> response = CommonResponse.<List<CustomerResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("List of Customer successfully retrieved")
                .data(customerResponses.getContent())
                .paginationResponse(paginationResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<CustomerResponse>> update(@RequestBody UpdateCustomerRequest request) {
        CustomerResponse updated = customerService.update(request);
        CommonResponse<CustomerResponse> response = CommonResponse.<CustomerResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Customer Updated")
                .data(updated)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<CommonResponse<String>> delete(@PathVariable String id) {
        customerService.delete(id);
        CommonResponse<String> response = CommonResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Customer Deleted")
                .build();
        return ResponseEntity.ok(response);
    }
}
