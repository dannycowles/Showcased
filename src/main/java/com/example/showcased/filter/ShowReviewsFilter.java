package com.example.showcased.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;

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
        if (request.getRequestURI().endsWith("/reviews") && request.getMethod().equals("POST") && session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            JSONObject responseObject = new JSONObject();
            responseObject.put("timestamp", java.time.Instant.now());
            responseObject.put("status", 401);
            responseObject.put("message", "You must be logged in to post reviews");
            responseObject.put("path", request.getRequestURI());
            response.getWriter().write(responseObject.toString());
            return;
        }

        // If the user is not logged in and attempts to like/unlike a show review we send an error
        if (request.getRequestURI().endsWith("/like") || request.getRequestURI().endsWith("/unlike") && session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            JSONObject responseObject = new JSONObject();
            responseObject.put("timestamp", java.time.Instant.now());
            responseObject.put("status", 401);
            responseObject.put("message", "You must be logged in to like/unlike show reviews");
            responseObject.put("path", request.getRequestURI());
            response.getWriter().write(responseObject.toString());
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
