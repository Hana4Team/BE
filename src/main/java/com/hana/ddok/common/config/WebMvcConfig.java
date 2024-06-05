package com.hana.ddok.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")
                .allowedOrigins("http://localhost:3000")
                .allowedOrigins("http://localhost:80")
                .allowedOrigins("http://localhost")
                .allowedOrigins("http://ec2-43-201-217-228.ap-northeast-2.compute.amazonaws.com")
                .allowedOrigins("http://ec2-43-201-217-228.ap-northeast-2.compute.amazonaws.com:80")
                .allowedOrigins("http://ec2-43-201-217-228.ap-northeast-2.compute.amazonaws.com:3000")
                .allowedOrigins("http://ec2-43-201-217-228.ap-northeast-2.compute.amazonaws.com:5173")
                .allowedMethods("OPTIONS", "GET", "POST", "PUT", "DELETE");
    }
}
