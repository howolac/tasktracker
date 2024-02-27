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
 * @since 2023-12-13
 */
@Data
@TableName("question")
@ApiModel(value = "Question表", description = "")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("问题")
    @TableField("ques_name")
    private String quesName;

    @ApiModelProperty("主键")
    @TableId(value = "ques_id", type = IdType.AUTO)
    private Integer quesId;

    @TableField("user_id")
    private Integer userId;

    @ApiModelProperty("什么情况下遇到这个问题的")
    @TableField("ques_circumstances")
    private String quesCircumstances;

    @ApiModelProperty("期望的结果")
    @TableField("ques_expectation")
    private String quesExpectation;

    @ApiModelProperty("实际的结果")
    @TableField("ques_actuality")
    private String quesActuality;

    @TableField("ques_create")
    private LocalDateTime quesCreate;

    @TableField("ques_modified")
    private LocalDateTime quesModified;

    @ApiModelProperty("解决的状态")
    @TableField("ques_status")
    private Integer quesStatus;


}




