package com.example.movieproject.config;


import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.UrlBasedViewResolverRegistration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig {

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000");//Set URL Domain được phép truy cập
        //config.setAllowedOrigin(List.of("*"));         nếu muốn linh hoạt
        config.addAllowedHeader("*");//Set Header mà Domain đó đươc truy cập(language)
        config.addAllowedMethod("*");//Set Method mà Domain được truy cập(PUT, POST, GET,...)-> sử dụng List.of
        source.registerCorsConfiguration("/**", config);//Set API mà Domain đó được truy cập(/** là tất cả, ví dụ /users/**)
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);// Thứ tụ chạy của filter CORS -> nhẹ nhất chạy đầu
        return bean;

    }
}
