package com.enigma.wmbapinext.service;

import com.enigma.wmbapinext.entity.TableMenu;

import java.util.List;

public interface TableService {
    TableMenu save(TableMenu tableMenu);

    TableMenu getById(String id);

    List<TableMenu> getAll();

    TableMenu update(TableMenu tableMenu);

    void delete(String id);
}
