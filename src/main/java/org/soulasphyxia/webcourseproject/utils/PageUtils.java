package org.soulasphyxia.webcourseproject.utils;


import org.springframework.data.domain.Pageable;

import java.util.List;

public class PageUtils {

    public static <T> List<T> getContentPage(Pageable pageable, List<T> content) {
        int start = (int)pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), content.size());
        return content.subList(start, end);
    }
}
