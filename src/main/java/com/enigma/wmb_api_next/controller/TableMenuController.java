package com.enigma.wmb_api_next.controller;

import com.enigma.wmb_api_next.constant.ApiUrl;
import com.enigma.wmb_api_next.entity.TableMenu;
import com.enigma.wmb_api_next.service.TableMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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
    public TableMenu save(@RequestBody TableMenu tableMenu) {
        return tableMenuService.save(tableMenu);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TableMenu> getALL() {
        return tableMenuService.getAll();
    }

    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public TableMenu getById(@PathVariable String id) {
        return tableMenuService.getById(id);
    }

    @PutMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public TableMenu update(@RequestBody TableMenu tableMenu) {
        return tableMenuService.update(tableMenu);
    }

    @DeleteMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String delete(@PathVariable String id) {
        tableMenuService.delete(id);
        return "Table Has Deleted";
    }
}