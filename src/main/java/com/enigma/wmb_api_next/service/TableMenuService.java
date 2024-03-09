package com.enigma.wmb_api_next.service;

import com.enigma.wmb_api_next.dto.request.TableMenuRequest;
import com.enigma.wmb_api_next.entity.TableMenu;

import java.util.List;

public interface TableMenuService {
    TableMenu save(TableMenuRequest request);

    TableMenu getById(String id);
    TableMenu getByName(String name);

    List<TableMenu> getAll();

    TableMenu update(TableMenu tableMenu);

    void delete(String id);
}
