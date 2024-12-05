package com.ojw.planner.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class CommonFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        //preflight request에 대해 인터셉터 통과
        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            response.setStatus(HttpStatus.OK.value());
            return;
        }

        //swagger 통과
        if(request.getRequestURI().contains("swagger")
                || request.getRequestURI().contains("api-docs")
                || request.getRequestURI().contains("webjars")
        ) {
            response.setStatus(HttpStatus.OK.value());
            return;
        }

        chain.doFilter(request, response);

    }

}
