package com.tasktracker.service;

import com.tasktracker.entity.Task;
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
public interface TaskService extends IService<Task> {
    void deleteTaskById(Integer taskId);

    void insertTask(Task task);

    List<Task> findTasks(int userId);

    List<Task> findTasksByTasklistId(Integer tasklistId);

    List<Task> findTasksStartingTodayOrderedByStartTime(int userId);

    List<Task> findTodayDeadlineTasks(int userId);

    List<Task> findTasksStartingTodayOrderedByStartTimeByUserId(Integer userId);

    List<Task> findTodayDeadlineTasksByUserId(Integer userId);

    void updateTask(Task task);

    void toggleTaskStatusById(@Param("taskId") Integer taskId);

    void resetTasklistIdForTasks(Integer tasklistId);

    int countTodayCompletedTasksByUserId(Integer userId);

    int countTasksStartingTodayByUserId(Integer userId);

    void resetCompletedTasksByUserId(Integer userId);
}
