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
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public Menu save(@RequestBody Menu menu) {
        return menuService.save(menu);
    }

    @PostMapping(
            path = "/bulk",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public List<Menu> saveBulk(@RequestBody List<Menu> menu) {
        return menuService.saveBulk(menu);
    }

    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Menu getById(@PathVariable String id) {
        return menuService.getById(id);
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Menu> getAll() {
        return menuService.getAll();
    }

    @PutMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public Menu update(@RequestBody Menu menu) {
        return menuService.update(menu);
    }

    @DeleteMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String delete(@PathVariable String id) {
        menuService.delete(id);
        return "Menu Has Deleted";
    }


}
