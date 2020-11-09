package com.czxy.redyu.config;

import com.czxy.redyu.security.filter.AdminAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.servlet.DispatcherType;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/14
 */
@Configuration
public class RedyuConfiguration {

    @Resource
    private AdminAuthenticationFilter  adminAuthenticationFilter;

    @Bean
    public FilterRegistrationBean<AdminAuthenticationFilter> filterRegistrationBean() {
        //设置不过滤路径
        adminAuthenticationFilter.addExcludeUrlPattens(
                "/admin/user/login",
                "/redis/*",
                "/js/**",
                "/css/**",
                "/fonts/**",
                "/content/**",
                "/img/**");

        FilterRegistrationBean<AdminAuthenticationFilter> filterReg = new FilterRegistrationBean<>();
        filterReg.setFilter(adminAuthenticationFilter);
        //优先级
        filterReg.setOrder(2);//值越小优先级越高

        filterReg.setDispatcherTypes(DispatcherType.REQUEST);
        //匹配路径
        List<String> urlPatterns = new ArrayList<>();
//        urlPatterns.add("/*");

        filterReg.addUrlPatterns("/*");
        filterReg.addInitParameter("exclusions","/gogo,/hello");
//        filterReg.setUrlPatterns(urlPatterns);
        System.out.println("过滤器注册执行");
        return filterReg;
    }

}
