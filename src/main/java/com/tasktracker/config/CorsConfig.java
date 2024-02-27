package com.tasktracker.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 解决跨域问题
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")// 配置作用路径
                // 允许的来源 ("*" 表示所有来源)
//                .allowedOrigins("*")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // 允许的请求方法
                .allowCredentials(true)// 允许发送身份验证信息（如 cookies）
                .maxAge(3600)
                .allowedHeaders("*")
                .exposedHeaders("*");  // 暴露响应头 (前端可通过Js代码获取响应头,"*"表示所有)
    }
}