package com.tasktracker.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private Integer userId; // 将userId声明为Integer类型
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean status;

    // 构造函数添加userId参数
    public CustomUserDetails(Integer userId, String username, String password,
                             String authorityString,
                             Boolean status) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.authorities = authoritiesFromString(authorityString);
        this.status = status;
    }

    //    将逗号隔开的权限字符串换成 GrantedAuthority 的形式
    private Collection<? extends GrantedAuthority> authoritiesFromString(String authorityString) {
        if (authorityString == null || authorityString.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(authorityString.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    // Getter方法以便于访问userId
    public Integer getUserId() {
        return userId;
    }

    // UserDetails接口方法的实现

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isEnabled() {
        return status;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

}