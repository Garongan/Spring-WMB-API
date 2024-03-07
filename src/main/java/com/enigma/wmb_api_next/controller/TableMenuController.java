package com.enigma.wmb_api_next.controller;

import com.enigma.wmb_api_next.constant.ApiUrl;
import com.enigma.wmb_api_next.constant.StatusMessege;
import com.enigma.wmb_api_next.dto.request.TableMenuRequest;
import com.enigma.wmb_api_next.dto.response.CommonResponse;
import com.enigma.wmb_api_next.entity.TableMenu;
import com.enigma.wmb_api_next.service.TableMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiUrl.API_URL + ApiUrl.TABLE_URL)
@RequiredArgsConstructor
public class TableMenuController {
    private final TableMenuService tableMenuService;

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<TableMenu>> save(@RequestBody TableMenuRequest request) {
        TableMenu tableMenu = tableMenuService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(getCommonResponse(tableMenu, HttpStatus.CREATED, StatusMessege.SUCCESS_CREATE));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<List<TableMenu>>> getALL() {
        List<TableMenu> tableMenus = tableMenuService.getAll();
        return ResponseEntity.ok(getCommonResponse(tableMenus));
    }

    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<TableMenu>> getById(@PathVariable String id) {
        TableMenu tableMenu = tableMenuService.getById(id);
        return ResponseEntity.ok(getCommonResponse(tableMenu, HttpStatus.OK, StatusMessege.SUCCESS_RETRIEVE));
    }

    @PutMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<TableMenu>> update(@RequestBody TableMenu tableMenu) {
        TableMenu update = tableMenuService.update(tableMenu);
        return ResponseEntity.ok(getCommonResponse(update, HttpStatus.OK, StatusMessege.SUCCESS_UPDATE));
    }

    @DeleteMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<String>> delete(@PathVariable String id) {
        tableMenuService.delete(id);
        CommonResponse<String> response = CommonResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message(StatusMessege.SUCCESS_DELETE)
                .build();
        return ResponseEntity.ok(response);
    }

    private CommonResponse<TableMenu> getCommonResponse(TableMenu tableMenu, HttpStatus status, String message) {
        return CommonResponse.<TableMenu>builder()
                .statusCode(status.value())
                .message(message)
                .data(tableMenu)
                .build();
    }

    private CommonResponse<List<TableMenu>> getCommonResponse(List<TableMenu> tableMenus) {
        return CommonResponse.<List<TableMenu>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(StatusMessege.SUCCESS_RETRIEVE_LIST)
                .data(tableMenus)
                .build();
    }
}