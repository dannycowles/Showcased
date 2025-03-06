package com.example.showcased.config;

import com.example.showcased.filter.ProfileFilter;
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
}
