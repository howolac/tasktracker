package com.tasktracker.config;

import com.tasktracker.security.handlers.CustomAuthenticationFailureHandler;
import com.tasktracker.security.handlers.CustomAuthenticationSuccessHandler;
import com.tasktracker.security.jwt.JWTUtils;
import com.tasktracker.security.jwt.JwtFilter;
import com.tasktracker.security.jwt.JwtProperties;
import com.tasktracker.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService customUserDetailsService;
    private final JWTUtils jwtUtils;
    private final JwtProperties jwtProperties;

    public WebSecurityConfig(CustomUserDetailsService customUserDetailsService, JWTUtils jwtUtils, JwtProperties jwtProperties) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtUtils = jwtUtils;
        this.jwtProperties = jwtProperties;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(jwtUtils, jwtProperties);
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler failureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService) // 使用自定义UserDetailsService
                .passwordEncoder(passwordEncoder()); // 使用BCryptPasswordEncoder进行密码加密
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 基于权限的访问控制
                .authorizeRequests()
                .antMatchers("/api/user/register").permitAll()
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .antMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")
                //                .antMatchers("/api/public/**").permitAll()
                .antMatchers("/api/**").hasRole("USER")
                .anyRequest().authenticated()

                .and()
                .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class) // 添加JwtFilter

                // 登出配置
                .logout()
                .permitAll()

                // 跨域和CSRF配置    
                .and()
                .cors().and()
                .csrf().disable()

                // 登录配置
                .formLogin()
                .loginProcessingUrl("/login").permitAll()
                .successHandler(successHandler())
                .failureHandler(failureHandler());
//                .successHandler((request, response, authentication) -> {
//                    System.out.println("Authentication success for user: " + authentication.getName());
//                })
//                .failureHandler((request, response, exception) -> {
//                    System.out.println("Authentication failed: " + exception.getMessage());
//                });
    }
}