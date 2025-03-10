package com.example.showcased.config;

import com.example.showcased.filter.ProfileFilter;
import com.example.showcased.filter.ShowReviewsFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

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
        registrationBean.addUrlPatterns("/show/*");
        registrationBean.setOrder(2);
        return registrationBean;
    }
}
