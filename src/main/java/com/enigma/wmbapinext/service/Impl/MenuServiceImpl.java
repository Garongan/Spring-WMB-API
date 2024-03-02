package com.enigma.wmbapinext.service.Impl;

import com.enigma.wmbapinext.entity.Menu;
import com.enigma.wmbapinext.repository.MenuRepository;
import com.enigma.wmbapinext.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {
    private final MenuRepository menuRepository;
    @Override
    public Menu save(Menu menu) {
        return menuRepository.saveAndFlush(menu);
    }

    @Override
    public List<Menu> saveBulk(List<Menu> menu) {
        return menuRepository.saveAllAndFlush(menu);
    }

    @Override
    public Menu getById(String id) {
        return menuRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu Id Not Found"));
    }

    @Override
    public List<Menu> getAll() {
        return menuRepository.findAll();
    }

    @Override
    public Menu update(Menu menu) {
        getById(menu.getId());
        return menuRepository.saveAndFlush(menu);
    }

    @Override
    public void delete(String id) {
        Menu menu = getById(id);
        menuRepository.delete(menu);
    }
}
