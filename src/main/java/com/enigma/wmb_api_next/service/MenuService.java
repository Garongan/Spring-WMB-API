package com.enigma.wmb_api_next.service;

import com.enigma.wmb_api_next.entity.Menu;

import java.util.List;

public interface MenuService {
    Menu save(Menu menu);

    List<Menu> saveBulk(List<Menu> menu);

    Menu getById(String id);

    List<Menu> getAll(String name, Long minPrice, Long maxPrice);

    Menu update(Menu menu);

    void delete(String id);
}
