package com.lostfound.config;

import com.lostfound.interceptor.JwtTokenUserInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * 配置类，注册web层相关组件
 */
@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private JwtTokenUserInterceptor jwtTokenUserInterceptor;

    /**
     * 注册自定义拦截器
     *
     * @param registry
     */
    protected void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(jwtTokenUserInterceptor)
//                .addPathPatterns("/user/**")
                .excludePathPatterns("/user/**")
                .excludePathPatterns("/user/wxlogin",
                        "/doc.html", // 放行 Swagger UI 页面
                        "/webjars/**", // 放行 Swagger 静态资源
                        "/v3/api-docs/**", // Springdoc 接口文档（3.x）
                        "/swagger-resources/**", // Springfox 资源（2.x）
                        "/swagger-ui/**" // Springfox UI（2.x）
                         );
    }

    /**
     * 设置静态资源映射
     * @param registry
     */
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
