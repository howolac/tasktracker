package com.tasktracker.mapper;

import com.tasktracker.entity.Task;
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
public interface TaskMapper extends BaseMapper<Task> {
    List<Task> findTasksByUserId(int userId);

    void insertTask(Task task);

    void updateTask(Task task);

    List<Task> findTasksByTasklistId(@Param("tasklistId") Integer tasklistId);

    void resetTasklistIdForTasks(@Param("tasklistId") Integer tasklistId);

    List<Task> findTasksStartingTodayOrderedByStartTime(Integer userId);

    List<Task> findTasksWithTodayDeadline(Integer userId);

    int countTodayCompletedTasksByUserId(Integer userId);

    int resetCompletedTasksByUserId(Integer userId);

    int countTasksStartingTodayByUserId(Integer userId);

}

