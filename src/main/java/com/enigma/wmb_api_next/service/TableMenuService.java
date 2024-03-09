package com.enigma.wmb_api_next.service;

import com.enigma.wmb_api_next.dto.request.TableMenuRequest;
import com.enigma.wmb_api_next.dto.request.UpdateTableMenuRequest;
import com.enigma.wmb_api_next.dto.response.TableMenuResponse;

import java.util.List;

public interface TableMenuService {
    TableMenuResponse save(TableMenuRequest request);
    TableMenuResponse getById(String id);
    TableMenuResponse getByName(String name);
    List<TableMenuResponse> getAll();
    TableMenuResponse update(UpdateTableMenuRequest request);
    void delete(String id);
}
