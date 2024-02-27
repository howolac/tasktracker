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

/**
 * <p>
 *
 * </p>
 *
 * @author lzx
 * @since 2023-12-09
 */
@Data
@TableName("task")
@ApiModel(value = "Task表", description = "")
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "task_id", type = IdType.AUTO)
    private Integer taskId;

    @ApiModelProperty("所属列表")
    @TableField("tasklist_id")
    private Integer tasklistId;

    @ApiModelProperty("任务名")
    @TableField("task_name")
    private String taskName;

    @TableField("task_deadline")
    private LocalDateTime taskDeadline;

    @ApiModelProperty("任务优先级")
    @TableField("task_necessity")
    private Integer taskNecessity;

    @ApiModelProperty("任务是否完成的状态")
    @TableField("task_status")
    private Integer taskStatus;

    @TableField("task_create")
    private LocalDateTime taskCreate;

    @TableField("task_modified")
    private LocalDateTime taskModified;

    @ApiModelProperty("任务开始时间")
    @TableField("task_start")
    private LocalDateTime taskStart;

    @ApiModelProperty("任务结束时间")
    @TableField("task_end")
    private LocalDateTime taskEnd;

    @ApiModelProperty("任务是否当天完成")
    @TableField("task_done")
    private int taskDone;
}




