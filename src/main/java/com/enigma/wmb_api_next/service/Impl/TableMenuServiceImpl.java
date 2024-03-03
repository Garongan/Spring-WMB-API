package com.enigma.wmb_api_next.service.Impl;

import com.enigma.wmb_api_next.entity.TableMenu;
import com.enigma.wmb_api_next.repository.TableMenuRepository;
import com.enigma.wmb_api_next.service.TableMenuService;
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

    @Override
    public TableMenu save(TableMenu tableMenu) {
        return tableMenuRepository.saveAndFlush(tableMenu);
    }

    @Override
    public TableMenu getById(String id) {
        return tableMenuRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Table Menu Not Found"));
    }

    @Override
    public List<TableMenu> getAll() {
        return tableMenuRepository.findAll();
    }

    @Override
    public TableMenu update(TableMenu tableMenu) {
        getById(tableMenu.getId());
        return tableMenuRepository.saveAndFlush(tableMenu);
    }

    @Override
    public void delete(String id) {
        TableMenu tableMenu = getById(id);
        tableMenuRepository.delete(tableMenu);
    }
}
