package com.api.Utils;

import org.springframework.data.domain.Pageable;

public class QueryUtils {
    public static String createQueryPagination(Pageable page) {
        return String.format(" LIMIT %s,%s ", (page.getPageNumber() * page.getPageSize()), page.getPageSize());
    }

}
