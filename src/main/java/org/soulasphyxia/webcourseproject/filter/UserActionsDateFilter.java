package org.soulasphyxia.webcourseproject.filter;

import org.soulasphyxia.webcourseproject.entity.UserAction;
import org.soulasphyxia.webcourseproject.entity.dto.UserActionsDateDto;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class UserActionsDateFilter implements Filter<UserActionsDateDto, UserAction>  {

    @Override
    public boolean isApplicable(UserActionsDateDto filter) {
        return  filter.getDate() != null &&
                filter.getTo() == null &&
                filter.getFrom() == null;
    }

    @Override
    public Specification<UserAction> apply(UserActionsDateDto filter) {
        return ((root, query, builder) -> builder.between(root.get("dateTime"), builder.literal(filter.getDate()), builder.literal(filter.getDate().plusDays(1))));
    }
}
