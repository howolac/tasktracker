package com.tasktracker.common.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class PasswordDto {

    @NotBlank(message = "原密码不能为空")
    private String currentPassword;

    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, message = "新密码至少需要6个字符")
    private String newPassword;

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}