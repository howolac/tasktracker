package com.tasktracker.common.dto;

import java.time.LocalDateTime;

public class UserInfoDto {

    private String userName;
    private LocalDateTime userCreate;
    private String userEmail;
    private int userIsEmailEnabled;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDateTime getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(LocalDateTime userCreate) {
        this.userCreate = userCreate;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getUserIsEmailEnabled() {
        return userIsEmailEnabled;
    }

    public void setUserIsEmailEnabled(int userIsEmailEnabled) {
        this.userIsEmailEnabled = userIsEmailEnabled;
    }
}