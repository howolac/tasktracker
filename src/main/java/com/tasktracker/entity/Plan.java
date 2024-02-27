package com.tasktracker.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
@TableName("plan")
@ApiModel(value = "Plan表", description = "")
public class Plan implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("计划任务的主键")
    @TableId(value = "plan_id", type = IdType.AUTO)
    private Integer planId;

    @ApiModelProperty("所属用户")
    @TableField("user_id")
    private Integer userId;

    @ApiModelProperty("计划任务的名称")
    @TableField("plan_name")
    private String planName;

    @ApiModelProperty("上次执行时间")
    @TableField("plan_last_perform")
    private LocalDateTime planLastPerform;

    @ApiModelProperty("下次执行时间")
    @TableField("plan_next_perform")
    private LocalDateTime planNextPerform;

    @ApiModelProperty("创建时间")
    @TableField("plan_create")
    private LocalDateTime planCreate;

    @ApiModelProperty("修改时间")
    @TableField("plan_modified")
    private LocalDateTime planModified;

    @ApiModelProperty("激活状态")
    @TableField("plan_status")
    private Integer planStatus;

    @ApiModelProperty("执行频率（每天、每周、每月）")
    @TableField("plan_frequency")
    private String planFrequency;

    @ApiModelProperty("每周哪个几天重复")
    @TableField("plan_day_of_week")
    private String planDayOfWeek;

    @ApiModelProperty("每个月哪几天重复")
    @TableField("plan_month_day")
    private String planMonthDay;

    @ApiModelProperty("具体的时间点")
    @TableField("plan_time")
    private LocalTime planTime;

    @ApiModelProperty("计划是否完成")
    @TableField("plan_done")
    private Integer planDone;

    @ApiModelProperty("计划执行次数")
    @TableField("executed_plans_count")
    private Integer executedPlansCount;

    @ApiModelProperty("计划执行总次数")
    @TableField("total_plans_count")
    private Integer totalPlansCount;
}




