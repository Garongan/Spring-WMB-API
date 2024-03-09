package com.enigma.wmb_api_next.controller;

import com.enigma.wmb_api_next.constant.ApiUrl;
import com.enigma.wmb_api_next.constant.StatusMessage;
import com.enigma.wmb_api_next.dto.response.CommonResponse;
import com.enigma.wmb_api_next.dto.response.TransTypeResponse;
import com.enigma.wmb_api_next.service.TransTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiUrl.API_URL + ApiUrl.TRANS_TYPE_URL)
public class TransTypeController {
    private final TransTypeService transTypeService;

    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<TransTypeResponse>> getById(@PathVariable String id) {
        TransTypeResponse response = transTypeService.getById(id);
        return ResponseEntity.ok(CommonResponse.<TransTypeResponse>builder()
                .message(StatusMessage.SUCCESS_RETRIEVE)
                .data(response)
                .build());
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<List<TransTypeResponse> >> getAll() {
        List<TransTypeResponse> responses = transTypeService.getAll();
        return ResponseEntity.ok(CommonResponse.<List<TransTypeResponse>>builder()
                .message(StatusMessage.SUCCESS_RETRIEVE)
                .data(responses)
                .build());
    }
}
