package com.enigma.wmb_api_next.controller;

import com.enigma.wmb_api_next.constant.ApiUrl;
import com.enigma.wmb_api_next.constant.StatusMessege;
import com.enigma.wmb_api_next.dto.request.TableMenuRequest;
import com.enigma.wmb_api_next.dto.request.UpdateTableMenuRequest;
import com.enigma.wmb_api_next.dto.response.CommonResponse;
import com.enigma.wmb_api_next.dto.response.TableMenuResponse;
import com.enigma.wmb_api_next.entity.TableMenu;
import com.enigma.wmb_api_next.service.TableMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiUrl.API_URL + ApiUrl.TABLE_URL)
@RequiredArgsConstructor
public class TableMenuController {
    private final TableMenuService tableMenuService;

    @Operation(summary = "Create new table menu")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<TableMenu>> save(@RequestBody TableMenuRequest request) {
        TableMenuResponse saved = tableMenuService.save(request);
        TableMenu tableMenu = TableMenu.builder()
                .id(saved.getId())
                .name(saved.getName())
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(getCommonResponse(tableMenu, HttpStatus.CREATED, StatusMessege.SUCCESS_CREATE));
    }

    @Operation(summary = "Get all table menu")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'USER')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<List<TableMenu>>> getALL() {
        List<TableMenuResponse> tableMenuResponses = tableMenuService.getAll();
        List<TableMenu> tableMenus = tableMenuResponses.stream()
                .map(tableMenuResponse -> TableMenu.builder()
                        .id(tableMenuResponse.getId())
                        .name(tableMenuResponse.getName())
                        .build())
                .toList();
        return ResponseEntity.ok(getCommonResponse(tableMenus));
    }

    @Operation(summary = "Get table menu by id")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'USER')")
    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<TableMenu>> getById(@PathVariable String id) {
        TableMenuResponse tableMenuResponse = tableMenuService.getById(id);
        TableMenu tableMenu = TableMenu.builder()
                .id(tableMenuResponse.getId())
                .name(tableMenuResponse.getName())
                .build();
        return ResponseEntity.ok(getCommonResponse(tableMenu, HttpStatus.OK, StatusMessege.SUCCESS_RETRIEVE));
    }

    @Operation(summary = "Update table menu")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    @PutMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<TableMenu>> update(@RequestBody UpdateTableMenuRequest request) {
        TableMenuResponse updated = tableMenuService.update(request);
        TableMenu tableMenu = TableMenu.builder()
                .id(updated.getId())
                .name(updated.getName())
                .build();
        return ResponseEntity.ok(getCommonResponse(tableMenu, HttpStatus.OK, StatusMessege.SUCCESS_UPDATE));
    }

    @Operation(summary = "Delete table menu")
    @SecurityRequirement(name = "Authorization")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
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