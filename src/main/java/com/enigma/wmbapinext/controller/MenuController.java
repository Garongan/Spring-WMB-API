package com.enigma.wmbapinext.controller;

import com.enigma.wmbapinext.constant.ApiUrl;
import com.enigma.wmbapinext.entity.Menu;
import com.enigma.wmbapinext.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiUrl.API_URL + ApiUrl.MENU_URL)
public class MenuController {
    private final MenuService menuService;

    @PostMapping(
            path = "/bulk",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public List<Menu> save(@RequestBody List<Menu> menu) {
        return menuService.saveBulk(menu);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Menu> getAll(){
        return menuService.getAll();
    }
}
