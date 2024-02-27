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
 * @since 2023-12-23
 */
@Data
@TableName("file")
@ApiModel(value = "File表", description = "")
public class File implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "file_id", type = IdType.AUTO)
    private Integer fileId;

    @ApiModelProperty("外键")
    @TableField("user_id")
    private Integer userId;

    @ApiModelProperty("文件名")
    @TableField("file_name")
    private String fileName;

    @ApiModelProperty("大小")
    @TableField("file_size")
    private Long fileSize;

    @ApiModelProperty("类型")
    @TableField("file_type")
    private String fileType;

    @ApiModelProperty("路径")
    @TableField("file_path")
    private String filePath;

    @ApiModelProperty("修改时间")
    @TableField("file_modified")
    private LocalDateTime fileModified;

    @ApiModelProperty("创建时间")
    @TableField("file_create")
    private LocalDateTime fileCreate;
}




