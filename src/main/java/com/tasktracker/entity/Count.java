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
 * @since 2023-12-28
 */
@Data
@TableName("count")
@ApiModel(value = "Count表", description = "")
public class Count implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "count_id", type = IdType.AUTO)
    private Integer countId;

    @ApiModelProperty("用户id")
    @TableField("user_id")
    private Integer userId;


    @ApiModelProperty("计算用户当天完成的任务数量")
    @TableField("count_task")
    private Integer countTask;

    @ApiModelProperty("计算用户当天完成的计划数量")
    @TableField("count_plan")
    private Integer countPlan;

    @ApiModelProperty("用户当天的总计划数量")
    @TableField("count_plan_total")
    private Integer countPlanTotal;

    @ApiModelProperty("用户当天的总任务数量")
    @TableField("count_task_total")
    private Integer countTaskTotal;


    @ApiModelProperty("记录创建的时间")
    @TableField("count_create")
    private LocalDateTime countCreate;

    @ApiModelProperty("记录修改的时间")
    @TableField("count_modified")
    private LocalDateTime countModified;

}




