package com.enigma.wmb_api_next.service.Impl;

import com.enigma.wmb_api_next.dto.request.TableMenuRequest;
import com.enigma.wmb_api_next.dto.request.UpdateTableMenuRequest;
import com.enigma.wmb_api_next.dto.response.TableMenuResponse;
import com.enigma.wmb_api_next.entity.TableMenu;
import com.enigma.wmb_api_next.repository.TableMenuRepository;
import com.enigma.wmb_api_next.service.TableMenuService;
import com.enigma.wmb_api_next.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class TableMenuServiceImpl implements TableMenuService {
    private final TableMenuRepository tableMenuRepository;
    private final ValidationUtil validationUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TableMenuResponse save(TableMenuRequest request) {
        validationUtil.validate(request);
        TableMenu tableMenu = TableMenu.builder().name(request.getName()).build();
        TableMenu saved = tableMenuRepository.saveAndFlush(tableMenu);
        return getResponse(saved);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TableMenuResponse getById(String id) {
        TableMenu tableMenu = tableMenuRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Table Menu Not Found"));
        return getResponse(tableMenu);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TableMenuResponse getByName(String name) {
        TableMenu tableMenu = tableMenuRepository.findByNameLikeIgnoreCase(name).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Table Menu Not Found"));
        return getResponse(tableMenu);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<TableMenuResponse> getAll() {
        List<TableMenu> tableMenus = tableMenuRepository.findAll();
        return tableMenus.stream().map(this::getResponse).toList();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TableMenuResponse update(UpdateTableMenuRequest request) {
        getById(request.getId());
        TableMenu saved = tableMenuRepository.saveAndFlush(
                TableMenu.builder()
                        .id(request.getId())
                        .name(request.getName())
                        .build()
        );
        return getResponse(saved);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String id) {
        TableMenuResponse tableMenu = getById(id);
        tableMenuRepository.delete(TableMenu.builder()
                .id(tableMenu.getId())
                .name(tableMenu.getName())
                .build());
    }

    private TableMenuResponse getResponse(TableMenu saved) {
        return TableMenuResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .build();
    }
}
