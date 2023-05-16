package com.balukonis.app.universitymicroserviceUI;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfigurer implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry){

        // PATH: /xxx/yyy : VIEW (Html): admin/dashboard.htm
        registry.addViewController("/register").setViewName("register");
    }
}

