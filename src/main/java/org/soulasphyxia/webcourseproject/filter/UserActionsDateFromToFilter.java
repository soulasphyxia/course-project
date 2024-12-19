package org.soulasphyxia.webcourseproject.filter;

import org.soulasphyxia.webcourseproject.entity.UserAction;
import org.soulasphyxia.webcourseproject.entity.dto.UserActionsDateDto;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserActionsDateFromToFilter implements Filter<UserActionsDateDto, UserAction> {

    @Override
    public boolean isApplicable(UserActionsDateDto filter) {
        return  filter.getDate() != null &&
                filter.getFrom() != null &&
                filter.getTo() != null;
    }

    @Override
    public Specification<UserAction> apply(UserActionsDateDto filter) {
        LocalDateTime from = filter.getDate().atTime(filter.getFrom());
        LocalDateTime to = filter.getDate().atTime(filter.getTo()).plusSeconds(1).minusNanos(1);
        return ((root, query, builder) -> builder.between(root.get("dateTime"), from, to));
    }
}
