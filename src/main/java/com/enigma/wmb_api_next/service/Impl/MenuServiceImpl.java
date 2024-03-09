package com.enigma.wmb_api_next.service.Impl;

import com.enigma.wmb_api_next.constant.ApiUrl;
import com.enigma.wmb_api_next.dto.request.MenuRequest;
import com.enigma.wmb_api_next.dto.request.SearchMenuRequest;
import com.enigma.wmb_api_next.dto.request.UpdateMenuRequest;
import com.enigma.wmb_api_next.dto.response.ImageResponse;
import com.enigma.wmb_api_next.dto.response.MenuResponse;
import com.enigma.wmb_api_next.entity.Image;
import com.enigma.wmb_api_next.entity.Menu;
import com.enigma.wmb_api_next.repository.MenuRepository;
import com.enigma.wmb_api_next.service.ImageService;
import com.enigma.wmb_api_next.service.MenuService;
import com.enigma.wmb_api_next.specification.MenuSpecification;
import com.enigma.wmb_api_next.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {
    private final MenuRepository menuRepository;
    private final MenuSpecification specification;
    private final ValidationUtil validationUtil;
    private final ImageService imageService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MenuResponse save(MenuRequest request) {
        validationUtil.validate(request);
        Image image = imageService.saveImage(request.getImage());
        return convertToResponse(menuRepository.saveAndFlush(Menu.builder()
                .name(request.getName())
                .price(request.getPrice())
                .image(image)
                .build()));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<MenuResponse> saveBulk(List<MenuRequest> menuRequestList) {
        validationUtil.validate(menuRequestList);
        List<Menu> newMenuList = menuRequestList.stream().map(
                menu -> Menu.builder()
                        .name(menu.getName())
                        .price(menu.getPrice())
                        .build()
        ).toList();
        menuRepository.saveAllAndFlush(newMenuList);
        return newMenuList.stream().map(this::convertToResponse).toList();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MenuResponse getById(String id) {
        Menu menu = menuRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu Not Found"));
        return convertToResponse(menu);
    }

    private Menu getSingleMenuById(String id) {
        return menuRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu Not Found"));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Menu getMenuById(String id) {
        return menuRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu Not Found"));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<MenuResponse> getAll(SearchMenuRequest request) {
        Specification<Menu> menuSpecification = specification.specification(request);

        Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());
        if (request.getPage() <= 0) request.setPage(1);
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());

        Page<Menu> menus = menuRepository.findAll(menuSpecification, pageable);
        return menus.stream().map(this::convertToResponse).toList();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MenuResponse update(UpdateMenuRequest request) {
        Menu newMenu = getSingleMenuById(request.getId());
        MultipartFile requestImage = request.getImage();
        Image oldImage = newMenu.getImage();

        if (requestImage != null) {
            Image image = imageService.saveImage(requestImage);
            newMenu.setImage(image);
            imageService.deleteById(oldImage.getId());
        }

        newMenu.setName(request.getName());
        newMenu.setPrice(request.getPrice());

        Menu saved = menuRepository.saveAndFlush(
                Menu.builder()
                        .id(newMenu.getId())
                        .name(newMenu.getName())
                        .price(newMenu.getPrice())
                        .image(newMenu.getImage())
                        .build()
        );
        return convertToResponse(saved);
    }

    @Transactional(rollbackFor = Exception.class)
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
                .imageResponse(
                        ImageResponse.builder()
                                .name(menu.getImage().getName())
                                .url(ApiUrl.API_URL + ApiUrl.MENU_URL + "/" + menu.getImage().getId() + "/images")
                                .build()
                )
                .build();
    }
}
