package com.enigma.wmb_api_next.service;

import com.enigma.wmb_api_next.entity.Image;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    Image saveImage(MultipartFile multipartFile);

    Resource getById(String id);

    void deleteById(String id);
}
