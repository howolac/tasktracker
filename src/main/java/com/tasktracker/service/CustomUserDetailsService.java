package com.tasktracker.service;

import com.tasktracker.entity.CustomUserDetails;
import com.tasktracker.mapper.CustomUserDetailsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomUserDetailsMapper customUserDetailsMapper;

    @Autowired
    public CustomUserDetailsService(CustomUserDetailsMapper customUserDetailsMapper) {
        this.customUserDetailsMapper = customUserDetailsMapper;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        从数据库获取用户信息，包括权限字符串
        CustomUserDetails customUserDetails = customUserDetailsMapper.loadUserByUsername(username);
        // 检查用户是否存在
        if (customUserDetails == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return customUserDetails;
    }
}