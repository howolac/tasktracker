package com.tasktracker.controller;


import com.tasktracker.common.dto.PasswordDto;
import com.tasktracker.common.dto.UserInfoDto;
import com.tasktracker.common.lang.Result;
import com.tasktracker.entity.User;
import com.tasktracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author user
 * @since 2023-11-14
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Result insertUser(@RequestBody User user) {
        try {
            int insertCount = userService.insertUser(user);
            if (insertCount > 0) {
                return Result.succ(null);
            } else {
                return Result.fail("用户创建失败");
            }
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    // 更新用户密码
    @PatchMapping("/reset-password")
    public Result updateUserPassword(@Valid @RequestBody PasswordDto passwordDto) {
        int isUpdated = userService.updateUserPassword(passwordDto);

        if (isUpdated > -1) {
            return Result.succ("密码更新成功");
        } else {
            return Result.fail("密码更新失败");
        }
    }

    @GetMapping("/info")
    public Result getUserInfo() {
        return Result.succ(userService.getUserInfo());
    }

    @PostMapping("/update")
    public Result updateUserInfo(@RequestBody UserInfoDto userInfoDto) {
        try {
            int updateCount = userService.updateUserInfo(userInfoDto);
            if (updateCount == 1) {
                return Result.succ("用户信息更新成功");
            } else {
                return Result.fail("未找到用户");
            }
        } catch (Exception e) {
            return Result.fail("更新用户信息时发生错误");
        }
    }
}
