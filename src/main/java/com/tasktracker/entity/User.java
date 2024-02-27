package com.tasktracker.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * <p>
 *
 * </p>
 *
 * @author user
 * @since 2023-11-14
 */
@Data
@TableName("user")
@ApiModel(value = "User表", description = "")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("用户id")
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty("用户名")
    @TableField("user_name")
    private String userName;

    @NotBlank(message = "用户密码不能为空")
    @ApiModelProperty("用户密码")
    @TableField("user_password")
    private String userPassword;

    //    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @ApiModelProperty("用户邮箱")
    @TableField("user_email")
    private String userEmail;

    @TableField("user_status")
    private Integer userStatus;

    @ApiModelProperty("账号创建时间")
    @TableField("user_create")
    private LocalDateTime userCreate;

    @ApiModelProperty("账号最后修改时间")
    @TableField("user_modified")
    private LocalDateTime userModified;

    @ApiModelProperty("账号最后登录时间")
    @TableField("user_last_login")
    private LocalDateTime userLastLogin;

    @ApiModelProperty("是否同意发送邮件")
    @TableField("user_is_email_enabled ")
    private Integer userIsEmailEnabled;

}




