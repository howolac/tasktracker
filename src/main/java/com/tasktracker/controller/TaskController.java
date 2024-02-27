package com.tasktracker.controller;

import com.tasktracker.common.lang.Result;
import com.tasktracker.entity.Task;
import com.tasktracker.service.TaskService;
import com.tasktracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    @Autowired
    private UserService userService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // 删除任务
    @DeleteMapping("/{id}")
    public Result deleteTaskById(@PathVariable Integer id) {
        taskService.deleteTaskById(id);
        return Result.succ(null);
    }

    // 创建任务
    @PostMapping("/insert")
    public Result insertTask(@RequestBody Task task) {
        taskService.insertTask(task);
        return Result.succ(null);
    }

    // 更新任务
    @PutMapping("/update")
    public Result updateTask(@RequestBody Task task) {
        taskService.updateTask(task);
        return Result.succ(task);
    }

    @GetMapping("/today-start")
    public Result getTasksStartingTodayOrderedByStartTime() {
        return Result.succ(taskService.findTasksStartingTodayOrderedByStartTime(userService.getCurrentUserId()));
    }

    @GetMapping("/today-deadline")
    public Result getTasksDeadlineTodayOrderedByDeadline() {
        return Result.succ(taskService.findTodayDeadlineTasks(userService.getCurrentUserId()));
    }

    @GetMapping
    public Result getAllTasks() {
        try {
            List<Task> tasks = taskService.findTasks(userService.getCurrentUserId());
            return Result.succ(tasks);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    // 根据id修改任务的完成状态
    @PutMapping("/toggle-status/{taskId}")
    public Result toggleTaskStatusById(@PathVariable Integer taskId) {
        try {
            taskService.toggleTaskStatusById(taskId);
            return Result.succ(null);
        } catch (Exception e) {
            return Result.fail("切换任务状态失败");
        }
    }


}