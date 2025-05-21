package io.d4tzz.newrms.service;

import io.d4tzz.newrms.security.JwtUserPrincipal;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;

public class AbstractService {
    protected Long getUserIdentity() {
        JwtUserPrincipal userPrincipal = (JwtUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userPrincipal.getIdentity();
    }

    protected Pageable ensureSortedPageable(Pageable pageable) {
        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by("createdAt").descending()
            );
        }

        return pageable;
    }
}
