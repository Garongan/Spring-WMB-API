package com.enigma.wmb_api_next.service.Impl;

import com.enigma.wmb_api_next.entity.Menu;
import com.enigma.wmb_api_next.repository.MenuRepository;
import com.enigma.wmb_api_next.service.MenuService;
import com.enigma.wmb_api_next.specification.MenuSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {
    private final MenuRepository menuRepository;
    private final MenuSpecification specification;
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
        return menuRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu Not Found"));
    }

    @Override
    public List<Menu> getAll(String name, Long minPrice, Long maxPrice) {
        Specification<Menu> menuSpecification = specification.specification(name, minPrice, maxPrice);
        return menuRepository.findAll(menuSpecification);
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
