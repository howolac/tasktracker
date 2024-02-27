package com.tasktracker.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author lzx
 * @since 2023-12-09
 */
@Data
@TableName("tasklist")
@ApiModel(value = "Tasklist表", description = "")
public class Tasklist implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "tasklist_id", type = IdType.AUTO)
    private Integer tasklistId;


    @ApiModelProperty("所属用户")
    @TableField("user_id")
    private Integer userId;

    @ApiModelProperty("任务列表名")
    @TableField("tasklist_name")
    private String tasklistName;

    @ApiModelProperty("备注")
    @TableField("tasklist_note")
    private String tasklistNote;

    @ApiModelProperty("创建时间")
    @TableField("tasklist_create")
    private LocalDateTime tasklistCreate;

    @ApiModelProperty("修改时间")
    @TableField("tasklist_modified")
    private LocalDateTime tasklistModified;

    private List<Task> tasks; // 任务列表
}




