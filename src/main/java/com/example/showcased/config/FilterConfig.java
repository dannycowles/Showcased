package com.example.showcased.config;

import com.example.showcased.filter.ProfileFilter;
import com.example.showcased.filter.ShowReviewsFilter;
import com.example.showcased.filter.UserFilter;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<Filter> corsFilter() {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter((req, res, chain) -> {
            HttpServletRequest request = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) res;
            response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type");
            response.setHeader("Access-Control-Allow-Credentials", "true");

            if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                chain.doFilter(req, res);
            }
        });
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<ProfileFilter> profileFilter() {
        FilterRegistrationBean<ProfileFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ProfileFilter());
        registrationBean.addUrlPatterns("/profile/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<ShowReviewsFilter> showReviewsFilter() {
        FilterRegistrationBean<ShowReviewsFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ShowReviewsFilter());
        registrationBean.addUrlPatterns("/shows/*");
        registrationBean.setOrder(2);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<UserFilter> userFilter() {
        FilterRegistrationBean<UserFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new UserFilter());
        registrationBean.addUrlPatterns("/user/*");
        registrationBean.setOrder(3);
        return registrationBean;
    }
}
