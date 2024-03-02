package com.enigma.wmb_api_next.specification;

import com.enigma.wmb_api_next.entity.Menu;
import jakarta.persistence.criteria.Predicate;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MenuSpecification {
    public Specification<Menu>
    specification(String name, Long minPrice, Long maxPrice) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null) {
                Predicate nameFilter = criteriaBuilder.like(criteriaBuilder.upper(root.get("name")), "%" + name.toUpperCase() + "%");
                predicates.add(nameFilter);
            }

            if (minPrice != null || maxPrice != null) {
                Long newMinPrice = minPrice == null ? Long.MIN_VALUE : minPrice;
                Long newMaxPrice = maxPrice == null ? Long.MAX_VALUE : maxPrice;
                Predicate priceFilter = criteriaBuilder.between(root.get("price"), newMinPrice, newMaxPrice);
                predicates.add(priceFilter);
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
    }
}
