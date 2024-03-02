package com.enigma.wmbapinext.service.Impl;

import com.enigma.wmbapinext.entity.Menu;
import com.enigma.wmbapinext.repository.MenuRepository;
import com.enigma.wmbapinext.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {
    private final MenuRepository menuRepository;
    @Override
    public Menu save(Menu menu) {
        return null;
    }

    @Override
    public List<Menu> saveBulk(List<Menu> menu) {
        return menuRepository.saveAllAndFlush(menu);
    }

    @Override
    public Menu getById(String id) {
        return null;
    }

    @Override
    public List<Menu> getAll() {
        return menuRepository.findAll();
    }

    @Override
    public Menu update(Menu menu) {
        return null;
    }

    @Override
    public void delete(String id) {

    }
}
