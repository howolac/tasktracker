package com.tasktracker.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tasktracker.entity.Tasklist;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author lzx
 * @since 2023-12-09
 */
public interface TasklistService extends IService<Tasklist> {
    int deleteTasklistById(int tasklistId);


    List<Tasklist> getAllTasklistsByUserId(int userId);

    int insertTasklist(Tasklist tasklist);

    int updateTasklist(Tasklist tasklist);

    IPage<Tasklist> searchTasklistsByOptionalText(@Param("page") Page<Tasklist> page, @Param("userId") Integer userId,
                                                  @Param("searchNameText") String searchNameText,
                                                  @Param("searchNoteText") String searchNoteText);

}
