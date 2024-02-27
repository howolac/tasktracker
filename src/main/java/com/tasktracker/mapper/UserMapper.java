package com.tasktracker.mapper;

import com.tasktracker.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author user
 * @since 2023-11-14
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    int insertUser(User user);

    int selectRoleIdByRoleName(@Param("roleName") String roleName);
    int insertUserRole(@Param("userId") Integer userId, @Param("roleId") Integer roleId);
    int updateUserPassword(@Param("userId") Integer userId, @Param("newPassword") String newPassword);

    Integer selectUserIdByUsername(@Param("username") String username);
    void updateUserLastLoginTime(Integer userId);


}
