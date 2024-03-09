package com.enigma.wmb_api_next.repository;

import com.enigma.wmb_api_next.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, String> {
}
