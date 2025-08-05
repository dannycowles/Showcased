package com.example.showcased.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * This filter is used whenever a user tries to add a review to a show
 * if the user is not logged into an account, they will not be able to
 * do so and will be redirected to the login page (update later)
 */
public class ShowReviewsFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();

        // If the user is not logged in we send an error and return
        if ((request.getRequestURI().endsWith("/reviews") || request.getRequestURI().endsWith("/comments")) && request.getMethod().equals("POST") && session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // If the user is not logged in and attempts to like/unlike a show review we send an error
        if ((request.getRequestURI().endsWith("/likes")) && session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // If the user is not logged in and attempts to delete items we send an error
        if ((request.getMethod().equals("DELETE") || request.getMethod().equals("PATCH")) && session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
