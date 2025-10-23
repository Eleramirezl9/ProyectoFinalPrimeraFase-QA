package com.ejemplo.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Filtro de debug para ver qué requests están llegando
 * TEMPORAL - Solo para debugging
 */
@Component
@Order(1)
public class DebugSecurityFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(DebugSecurityFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;

            logger.info("========================================");
            logger.info("DEBUG SECURITY FILTER");
            logger.info("Method: {}", httpRequest.getMethod());
            logger.info("Request URI: {}", httpRequest.getRequestURI());
            logger.info("Context Path: {}", httpRequest.getContextPath());
            logger.info("Servlet Path: {}", httpRequest.getServletPath());
            logger.info("Path Info: {}", httpRequest.getPathInfo());
            logger.info("Auth Header: {}", httpRequest.getHeader("Authorization"));
            logger.info("========================================");
        }

        chain.doFilter(request, response);
    }
}
