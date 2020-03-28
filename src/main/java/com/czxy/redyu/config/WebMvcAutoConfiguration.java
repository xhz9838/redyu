package com.czxy.redyu.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/11
 */
@Configuration
public class WebMvcAutoConfiguration implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
      String str = "file:///D://";
        registry.addResourceHandler("/music/**")
                .addResourceLocations(str+"/");
    }
}
