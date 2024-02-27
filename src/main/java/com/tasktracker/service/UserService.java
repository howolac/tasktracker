package com.tasktracker.service;

import com.tasktracker.common.dto.PasswordDto;
import com.tasktracker.common.dto.UserInfoDto;
import com.tasktracker.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author user
 * @since 2023-11-14
 */
public interface UserService extends IService<User> {
    int insertUser(User user);

    // 修改用户密码
    int updateUserPassword(PasswordDto passwordDto);

    UserInfoDto getUserInfo();

    int updateUserInfo(UserInfoDto userInfoDto);

    Integer getCurrentUserId();

    String getCurrentUsername();


    List<User> selectAllUser();

    void updateUserLastLoginTime(Integer userId);

}
