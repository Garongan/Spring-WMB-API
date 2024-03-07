package com.enigma.wmb_api_next.service.Impl;

import com.enigma.wmb_api_next.dto.request.MenuRequest;
import com.enigma.wmb_api_next.dto.response.MenuResponse;
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
    public MenuResponse save(MenuRequest menu) {
        return convertToResponse(menuRepository.saveAndFlush(Menu.builder()
                .name(menu.getName())
                .price(menu.getPrice())
                .build()));
    }

    @Override
    public List<MenuResponse> saveBulk(List<MenuRequest> menuRequestList) {
        List<Menu> newMenuList = menuRequestList.stream().map(
                menu -> Menu.builder()
                    .name(menu.getName())
                    .price(menu.getPrice())
                    .build()
        ).toList();
        menuRepository.saveAllAndFlush(newMenuList);
        return newMenuList.stream().map(this::convertToResponse).toList();
    }

    @Override
    public MenuResponse getById(String id) {
        Menu menu = menuRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu Not Found"));
        return convertToResponse(menu);
    }

    @Override
    public Menu getMenuById(String id) {
        return menuRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu Not Found"));
    }

    @Override
    public List<MenuResponse> getAll(String name, Long minPrice, Long maxPrice) {
        Specification<Menu> menuSpecification = specification.specification(name, minPrice, maxPrice);
        List<Menu> menuList = menuRepository.findAll(menuSpecification);
        return menuList.stream().map(this::convertToResponse).toList();
    }

    @Override
    public MenuResponse update(Menu menu) {
        getById(menu.getId());
        Menu saved = menuRepository.saveAndFlush(menu);
        return convertToResponse(saved);
    }

    @Override
    public void delete(String id) {
        Menu menu = getMenuById(id);
        menuRepository.delete(menu);
    }

    private MenuResponse convertToResponse(Menu menu) {
        return MenuResponse.builder()
                .id(menu.getId())
                .name(menu.getName())
                .price(menu.getPrice())
                .build();
    }
}
