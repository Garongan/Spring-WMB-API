package com.enigma.wmb_api_next.specification;

import com.enigma.wmb_api_next.entity.Customer;
import jakarta.persistence.criteria.Predicate;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class CustomerSpecification {

    public Specification<Customer> specification (String name){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null) {
                Predicate nameFilter = criteriaBuilder.like(criteriaBuilder.upper(root.get("name")), "%" + name.toUpperCase() + "%");
                predicates.add(nameFilter);
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
    }
}
