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
                .allowedOrigins("http://localhost:3000","http://localhost:80","http://localhost","http://ec2-43-201-217-228.ap-northeast-2.compute.amazonaws.com","http://ec2-43-201-217-228.ap-northeast-2.compute.amazonaws.com:3000","http://ec2-43-201-217-228.ap-northeast-2.compute.amazonaws.com:8080")
                .allowedMethods("OPTIONS", "GET", "POST", "PUT", "DELETE")
                .allowCredentials(true);
    }
}
