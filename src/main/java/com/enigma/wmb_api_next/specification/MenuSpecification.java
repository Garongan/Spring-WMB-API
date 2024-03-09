package com.enigma.wmb_api_next.specification;

import com.enigma.wmb_api_next.dto.request.SearchMenuRequest;
import com.enigma.wmb_api_next.entity.Menu;
import jakarta.persistence.criteria.Predicate;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MenuSpecification {
    public Specification<Menu>
    specification(SearchMenuRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getName() != null) {
                Predicate nameFilter = criteriaBuilder.like(criteriaBuilder.upper(root.get("name")), "%" + request.getName().toUpperCase() + "%");
                predicates.add(nameFilter);
            }

            if (request.getMinPrice() != null || request.getMaxPrice() != null) {
                Long newMinPrice = request.getMinPrice() == null ? Long.MIN_VALUE : request.getMinPrice();
                Long newMaxPrice = request.getMaxPrice() == null ? Long.MAX_VALUE : request.getMaxPrice();
                Predicate priceFilter = criteriaBuilder.between(root.get("price"), newMinPrice, newMaxPrice);
                predicates.add(priceFilter);
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
    }
}
