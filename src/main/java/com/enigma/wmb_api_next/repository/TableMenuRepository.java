package com.enigma.wmb_api_next.repository;

import com.enigma.wmb_api_next.entity.TableMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TableMenuRepository extends JpaRepository<TableMenu, String> {
    Optional<TableMenu> findByNameLikeIgnoreCase(String name);
}
