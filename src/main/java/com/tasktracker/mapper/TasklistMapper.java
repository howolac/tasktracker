package com.tasktracker.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tasktracker.entity.Tasklist;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author lzx
 * @since 2023-12-09
 */
@Mapper
public interface TasklistMapper extends BaseMapper<Tasklist> {
    int deleteTasklistById(int tasklistId);

    List<Tasklist> selectAllTasklistsByUserId(int userId);

    int insertTasklist(Tasklist tasklist);

    int updateTasklist(Tasklist tasklist);

    IPage<Tasklist> searchTasklistsByOptionalText(@Param("page") Page<Tasklist> page, @Param("userId") Integer userId,
                                                  @Param("searchNameText") String searchNameText,
                                                  @Param("searchNoteText") String searchNoteText);
}
