package com.amazone_clone.Order_service.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

public class FeignClientConfig implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {

        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            return;
        }

        HttpServletRequest request = attributes.getRequest();

        String authorization =
                request.getHeader("Authorization");

        String userEmail =
                request.getHeader("X-User-Email");

        String userRole =
                request.getHeader("X-User-Role");

        if (authorization != null) {
            template.header("Authorization", authorization);
        }

        if (userEmail != null) {
            template.header("X-User-Email", userEmail);
        }

        if (userRole != null) {
            template.header("X-User-Role", userRole);
        }
    }
}