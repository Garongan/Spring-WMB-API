package com.enigma.wmb_api_next.specification;

import com.enigma.wmb_api_next.dto.request.SearchBillRequest;
import com.enigma.wmb_api_next.entity.Bill;
import jakarta.persistence.criteria.Predicate;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Configuration
public class BillSpecification {
    public Specification<Bill> specification (SearchBillRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getDaily() != null) {
                predicates.add(criteriaBuilder.equal(root.get("transDate"), request.getDaily()));
            }

            if (request.getWeeklyStart() != null && request.getWeeklyEnd() != null) {
                predicates.add(criteriaBuilder.between(root.get("transDate"), request.getWeeklyStart(), request.getWeeklyEnd()));
            }

            if (request.getMonthly() != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(request.getMonthly().getTime());
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                Predicate monthFilter = criteriaBuilder.equal(root.get("transDate").get("month"), month);
                Predicate yearFilter = criteriaBuilder.equal(root.get("transDate").get("year"), year);
                predicates.add(criteriaBuilder.and(monthFilter, yearFilter));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
    }
}
