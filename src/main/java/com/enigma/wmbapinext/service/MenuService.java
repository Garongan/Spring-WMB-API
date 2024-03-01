package com.enigma.wmbapinext.service;

import com.enigma.wmbapinext.entity.Menu;

import java.util.List;

public interface MenuService {
    Menu save(Menu menu);

    Menu getById(String id);

    List<Menu> getAll();

    Menu update(Menu menu);

    void delete(String id);
}
