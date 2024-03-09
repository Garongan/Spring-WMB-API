package com.enigma.wmb_api_next.service.Impl;

import com.enigma.wmb_api_next.constant.StatusMessage;
import com.enigma.wmb_api_next.entity.Image;
import com.enigma.wmb_api_next.repository.ImageRepository;
import com.enigma.wmb_api_next.service.ImageService;
import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private final Path path;

    @Autowired
    public ImageServiceImpl(
             ImageRepository imageRepository,
             @Value("${wmb_api_next.image.path}") String path) {
        this.imageRepository = imageRepository;
        this.path = Paths.get(path);
    }

    @PostConstruct
    public void initPath() {
        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, StatusMessage.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Image saveImage(MultipartFile multipartFile) {
        try {
            if (!List.of("image/jpg", "image/jpeg", "image/png", "image/svg+xml").contains(multipartFile.getContentType()))
                throw new ConstraintViolationException("invalid image type", null);
            String fileName = System.currentTimeMillis() + multipartFile.getName();
            Path filePath = path.resolve(fileName);
            Files.copy(multipartFile.getInputStream(), filePath);

            Image image = Image.builder()
                    .name(fileName)
                    .path(filePath.toString())
                    .size(multipartFile.getSize())
                    .contentType(multipartFile.getContentType())
                    .build();

            return imageRepository.saveAndFlush(image);

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, StatusMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Resource getById(String id) {
        try {
            Image image = imageRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, StatusMessage.IMAGE_NOT_FOUND));
            Path filePath = Paths.get(image.getPath());
            if (!Files.exists(filePath))
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, StatusMessage.IMAGE_NOT_FOUND);
            return new UrlResource(filePath.toUri());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, StatusMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void deleteById(String id) {
        try {
            Image image = imageRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, StatusMessage.IMAGE_NOT_FOUND));
            Path filePath = Paths.get(image.getPath());
            if (!Files.exists(filePath))
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, StatusMessage.IMAGE_NOT_FOUND);
            Files.delete(filePath);
            imageRepository.deleteById(id);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, StatusMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
