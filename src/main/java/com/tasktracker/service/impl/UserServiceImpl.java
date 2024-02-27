package com.tasktracker.service.impl;

import com.tasktracker.common.dto.PasswordDto;
import com.tasktracker.common.dto.UserInfoDto;
import com.tasktracker.entity.User;
import com.tasktracker.mapper.UserMapper;
import com.tasktracker.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author user
 * @since 2023-11-14
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder; // 注入PasswordEncoder

    @Autowired
    public UserServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }

    public Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            if (username != null) {
                return userMapper.selectUserIdByUsername(username);
            }
        }
        return null;
    }

    @Override
    public List<User> selectAllUser() {
        return list();
    }

    @Override
    public void updateUserLastLoginTime(Integer userId) {
        userMapper.updateUserLastLoginTime(userId);
    }

    @Override
    public int insertUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.getUserPassword());
        user.setUserPassword(encodedPassword);

        int insertCount = userMapper.insertUser(user);
//        默认是普通用户
        int roleId = userMapper.selectRoleIdByRoleName("ROLE_USER");
        userMapper.insertUserRole(user.getUserId(), roleId);

        return insertCount;
    }

    @Override
    public int updateUserPassword(PasswordDto passwordDto) {
//        验证原来的密码
        User currentUser = userMapper.selectById(getCurrentUserId());
        if (!passwordEncoder.matches(passwordDto.getCurrentPassword(), currentUser.getUserPassword())) {
            System.out.println("密码错误");
            return -1;
        }
//        对密码编码再更新
        String encodedNewPassword = passwordEncoder.encode(passwordDto.getNewPassword());
        return userMapper.updateUserPassword(getCurrentUserId(), encodedNewPassword);
    }

    @Override
    public UserInfoDto getUserInfo() {
        User user = userMapper.selectById(getCurrentUserId());
        UserInfoDto userInfoDto = new UserInfoDto();

        userInfoDto.setUserEmail(user.getUserEmail());
        userInfoDto.setUserName(user.getUserName());
        userInfoDto.setUserCreate(user.getUserCreate());
        userInfoDto.setUserIsEmailEnabled(user.getUserIsEmailEnabled());

        return userInfoDto;
    }

    @Override
    public int updateUserInfo(UserInfoDto userInfoDto) {
        User user = userMapper.selectById(getCurrentUserId());
        user.setUserEmail(userInfoDto.getUserEmail());
        user.setUserIsEmailEnabled(userInfoDto.getUserIsEmailEnabled());
        return userMapper.updateById(user);
    }
}
