package org.soulasphyxia.webcourseproject.filter;

import org.soulasphyxia.webcourseproject.entity.UserAction;
import org.soulasphyxia.webcourseproject.entity.dto.UserActionsDateDto;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class UserActionsDateFromFilter implements Filter<UserActionsDateDto, UserAction> {

    @Override
    public boolean isApplicable(UserActionsDateDto filter) {
        return  filter.getDate() != null &&
                filter.getFrom() != null &&
                filter.getTo() == null;
    }

    @Override
    public Specification<UserAction> apply(UserActionsDateDto filter) {
        LocalDateTime filterDateTime = filter.getDate().atTime(filter.getFrom());
        LocalDateTime currentDayEnd = filter.getDate().plusDays(1).atTime(LocalTime.MIDNIGHT);
        return ((root, query, builder) -> builder.and(
                builder.between(root.get("dateTime"), builder.literal(filterDateTime), builder.literal(currentDayEnd))
        ));
    }
}
