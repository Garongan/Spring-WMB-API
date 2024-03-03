package com.enigma.wmb_api_next.service;

import com.enigma.wmb_api_next.entity.TableMenu;

import java.util.List;

public interface TableMenuService {
    TableMenu save(TableMenu tableMenu);

    TableMenu getById(String id);

    List<TableMenu> getAll();

    TableMenu update(TableMenu tableMenu);

    void delete(String id);
}
