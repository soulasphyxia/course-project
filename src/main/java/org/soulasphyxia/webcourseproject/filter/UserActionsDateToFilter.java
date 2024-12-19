package org.soulasphyxia.webcourseproject.filter;

import org.soulasphyxia.webcourseproject.entity.UserAction;
import org.soulasphyxia.webcourseproject.entity.dto.UserActionsDateDto;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class UserActionsDateToFilter implements Filter<UserActionsDateDto, UserAction> {

    @Override
    public boolean isApplicable(UserActionsDateDto filter) {
        return  filter.getDate() != null &&
                filter.getTo() != null &&
                filter.getFrom() == null;
    }

    @Override
    public Specification<UserAction> apply(UserActionsDateDto filter) {
        LocalDateTime to = filter.getDate().atTime(filter.getTo());
        LocalDateTime currentDayStart = filter.getDate().atTime(LocalTime.MIDNIGHT);
        return ((root, query, builder) -> builder.between(root.get("dateTime"), currentDayStart, to));
    }
}
