package org.soulasphyxia.webcourseproject.filter;

import org.springframework.data.jpa.domain.Specification;

public interface Filter<T, E> {

    boolean isApplicable(T filter);

    Specification<E> apply(T filter);
}
