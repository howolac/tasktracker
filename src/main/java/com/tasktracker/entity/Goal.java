package com.tasktracker.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author lzx
 * @since 2023-12-07
 */
@Data
@TableName("goal")
@ApiModel(value = "Goal表", description = "")
public class Goal implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "goal_id", type = IdType.AUTO)
    private Integer goalId;

    @ApiModelProperty("用户id")
    @TableField("user_id")
    private Integer userId;

    @ApiModelProperty("标题，比如本月目标")
    @TableField("goal_title")
    private String goalTitle;

    @ApiModelProperty("内容")
    @TableField("goal_content")
    private String goalContent;

    @TableField("goal_create")
    private LocalDateTime goalCreate;

    @TableField("goal_modified")
    private LocalDateTime goalModified;


}




