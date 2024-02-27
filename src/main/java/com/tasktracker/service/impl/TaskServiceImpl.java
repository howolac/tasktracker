package com.tasktracker.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.tasktracker.entity.Task;
import com.tasktracker.entity.Tasklist;
import com.tasktracker.mapper.TaskMapper;
import com.tasktracker.mapper.UserMapper;
import com.tasktracker.service.TaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tasktracker.service.TasklistService;
import com.tasktracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lzx
 * @since 2023-12-09
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {
    private final TaskMapper taskMapper;

    @Autowired
    public TaskServiceImpl(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;

    }

    @Override
    public void deleteTaskById(Integer taskId) {
        taskMapper.deleteById(taskId);
    }

    @Override
    public void insertTask(Task task) {
        taskMapper.insertTask(task);
    }


    @Override
    public List<Task> findTasksByTasklistId(Integer tasklistId) {
        return taskMapper.findTasksByTasklistId(tasklistId);
    }

    @Override
    public List<Task> findTasks(int userId) {
        List<Task> tasks = taskMapper.findTasksByUserId(userId);
        return tasks;
    }

    @Override
    public void updateTask(Task task) {
        taskMapper.updateTask(task);
    }

    public void toggleTaskStatusById(Integer taskId) {
        Task task = taskMapper.selectById(taskId);
        boolean isToday = task.getTaskStart().toLocalDate().isEqual(LocalDate.now());

        if (task.getTaskStatus() == 0) {
            task.setTaskStatus(1);
            if (isToday) {//如果开始时间是今天，加入统计数据
                task.setTaskDone(1);
            }
        } else {
            task.setTaskStatus(0);
            if (isToday) {
                task.setTaskDone(1);
            }
            task.setTaskDone(0);
        }
        updateTask(task);
    }

    @Override
    public void resetTasklistIdForTasks(Integer tasklistId) {
        taskMapper.resetTasklistIdForTasks(tasklistId);
    }

    @Override
    public int countTodayCompletedTasksByUserId(Integer userId) {
        return taskMapper.countTodayCompletedTasksByUserId(userId);
    }

    @Override
    public int countTasksStartingTodayByUserId(Integer userId) {
        return taskMapper.countTasksStartingTodayByUserId(userId);
    }

    @Override
    public void resetCompletedTasksByUserId(Integer userId) {
        taskMapper.resetCompletedTasksByUserId(userId);
    }

    @Override
    public List<Task> findTasksStartingTodayOrderedByStartTime(int userId) {
        return taskMapper.findTasksStartingTodayOrderedByStartTime(userId);
    }

    // 查找今天截止的任务
    public List<Task> findTodayDeadlineTasks(int userId) {
        return taskMapper.findTasksWithTodayDeadline(userId);
    }

    @Override
    public List<Task> findTasksStartingTodayOrderedByStartTimeByUserId(Integer userId) {
        return taskMapper.findTasksStartingTodayOrderedByStartTime(userId);

    }

    @Override
    public List<Task> findTodayDeadlineTasksByUserId(Integer userId) {
        return taskMapper.findTasksWithTodayDeadline(userId);
    }
}
