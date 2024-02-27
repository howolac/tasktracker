package com.tasktracker.mapper;

import com.tasktracker.entity.CustomUserDetails;
import com.tasktracker.entity.Task;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomUserDetailsMapper {
    CustomUserDetails loadUserByUsername(String username);
}
