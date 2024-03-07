package com.enigma.wmb_api_next.service;

import com.enigma.wmb_api_next.dto.request.MenuRequest;
import com.enigma.wmb_api_next.dto.response.MenuResponse;
import com.enigma.wmb_api_next.entity.Menu;

import java.util.List;

public interface MenuService {
    MenuResponse save(MenuRequest menu);

    List<MenuResponse> saveBulk(List<MenuRequest> menuRequestList);

    MenuResponse getById(String id);
    Menu getMenuById(String id);

    List<MenuResponse> getAll(String name, Long minPrice, Long maxPrice);

    MenuResponse update(Menu menu);

    void delete(String id);
}
