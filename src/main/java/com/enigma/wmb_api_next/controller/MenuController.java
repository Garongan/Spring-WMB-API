package com.enigma.wmb_api_next.controller;

import com.enigma.wmb_api_next.constant.ApiUrl;
import com.enigma.wmb_api_next.constant.StatusMessege;
import com.enigma.wmb_api_next.dto.request.MenuRequest;
import com.enigma.wmb_api_next.dto.response.CommonResponse;
import com.enigma.wmb_api_next.dto.response.MenuResponse;
import com.enigma.wmb_api_next.entity.Menu;
import com.enigma.wmb_api_next.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiUrl.API_URL + ApiUrl.MENU_URL)
public class MenuController {
    private final MenuService menuService;

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<MenuResponse>> save(@RequestBody MenuRequest menu) {
        MenuResponse response = menuService.save(menu);
        return ResponseEntity.status(HttpStatus.CREATED).body(getCommonResponse(response, HttpStatus.CREATED, StatusMessege.SUCCESS_CREATE));
    }

    @PostMapping(
            path = "/bulk",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<List<MenuResponse>>> saveBulk(@RequestBody List<MenuRequest> menu) {
        List<MenuResponse> menuResponses = menuService.saveBulk(menu);
        return ResponseEntity.status(HttpStatus.CREATED).body(getCommonResponseList(menuResponses, HttpStatus.CREATED, StatusMessege.SUCCESS_CREATE_LIST));
    }

    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<MenuResponse>> getById(@PathVariable String id) {
        MenuResponse response = menuService.getById(id);
        return ResponseEntity.ok(getCommonResponse(response, HttpStatus.OK, StatusMessege.SUCCESS_RETRIEVE));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<List<MenuResponse>>> getAll(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "minPrice", required = false) Long minPrice,
            @RequestParam(name = "maxPrice", required = false) Long maxPrice
    ) {
        List<MenuResponse> menuResponses = menuService.getAll(name, minPrice, maxPrice);
        return ResponseEntity.ok(getCommonResponseList(menuResponses, HttpStatus.OK, StatusMessege.SUCCESS_RETRIEVE_LIST));
    }

    @PutMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<MenuResponse>> update(@RequestBody Menu menu) {
        MenuResponse response = menuService.update(menu);
        return ResponseEntity.ok(getCommonResponse(response, HttpStatus.OK, StatusMessege.SUCCESS_UPDATE));
    }

    @DeleteMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<String>> delete(@PathVariable String id) {
        menuService.delete(id);
        CommonResponse<String> response = CommonResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message(StatusMessege.SUCCESS_DELETE)
                .build();
        return ResponseEntity.ok(response);
    }

    private CommonResponse<MenuResponse> getCommonResponse(MenuResponse menuResponse, HttpStatus status, String message) {
        return CommonResponse.<MenuResponse>builder()
                .statusCode(status.value())
                .message(message)
                .data(menuResponse)
                .build();
    }

    private CommonResponse<List<MenuResponse>> getCommonResponseList(List<MenuResponse> menuResponses, HttpStatus status, String message) {
        return CommonResponse.<List<MenuResponse>>builder()
                .statusCode(status.value())
                .message(message)
                .data(menuResponses)
                .build();
    }

}
