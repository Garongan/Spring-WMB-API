package com.enigma.wmb_api_next.specification;

import com.enigma.wmb_api_next.dto.request.SearchBillRequest;
import com.enigma.wmb_api_next.entity.Bill;
import com.enigma.wmb_api_next.util.DateUtil;
import jakarta.persistence.criteria.Predicate;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Configuration
public class BillSpecification {
    public Specification<Bill> specification (SearchBillRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getDaily() != null) {
                Date date = DateUtil.parseDate(request.getDaily());
                predicates.add(criteriaBuilder.equal(root.get("transDate"), date));
            }

            if (request.getWeeklyStart() != null && request.getWeeklyEnd() != null) {
                Date startDate = DateUtil.parseDate(request.getWeeklyStart());
                Date endDate = DateUtil.parseDate(request.getWeeklyEnd());
                predicates.add(criteriaBuilder.between(root.get("transDate"), startDate, endDate));
            }

            if (request.getMonthly() != null) {
                Date date = DateUtil.parseDate(request.getMonthly());
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(date.getTime());
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
