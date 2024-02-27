package com.tasktracker.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tasktracker.common.lang.Result;
import com.tasktracker.entity.Goal;
import com.tasktracker.entity.Task;
import com.tasktracker.entity.Tasklist;
import com.tasktracker.service.TaskService;
import com.tasktracker.service.TasklistService;
import com.tasktracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasklists")
public class TasklistController {

    private final TasklistService tasklistService;
    private final TaskService taskService;
    @Autowired
    private UserService userService;

    @Autowired
    public TasklistController(TasklistService tasklistService, TaskService taskService) {
        this.tasklistService = tasklistService;
        this.taskService = taskService;
    }

    @GetMapping("/search")
    public Result searchTasklists(
            @RequestParam int page, @RequestParam int limit,
            @RequestParam(value = "searchNameText", required = false) String searchNameText,
            @RequestParam(value = "searchNoteText", required = false) String searchNoteText) {
        try {
            Page<Tasklist> pageReq = new Page<>(page, limit); // 第page页，每页limit条数据
            IPage<Tasklist> tasklistPage = tasklistService.searchTasklistsByOptionalText(pageReq, userService.getCurrentUserId(), searchNameText, searchNoteText);
            return Result.succ(tasklistPage);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    @GetMapping("/with-tasks")
    public Result getAllTasklistsByUserIdWithTasks() {
        try {
            List<Tasklist> tasklists = tasklistService.getAllTasklistsByUserId(userService.getCurrentUserId());
            for (Tasklist tasklist : tasklists) {
                tasklist.setTasks(taskService.findTasksByTasklistId(tasklist.getTasklistId()));   // 为每个任务列表对象添加任务数组
            }
            return Result.succ(tasklists);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    @PostMapping("/add")
    public Result createTasklist(@RequestBody Tasklist tasklist) {
        tasklist.setUserId(userService.getCurrentUserId());
        int result = tasklistService.insertTasklist(tasklist);
        if (result == 1) {
            return Result.succ(null);
        } else {
            return Result.fail("创建任务列表失败");
        }
    }

    @PutMapping("/update")
    public Result updateTasklist(@RequestBody Tasklist tasklist) {
        int result = tasklistService.updateTasklist(tasklist);
        if (result == 1) {
            return Result.succ(null);
        } else {
            return Result.fail("更新任务列表失败");
        }
    }

    @DeleteMapping("delete/{tasklistId}")
    public Result deleteTasklist(@PathVariable int tasklistId) {
        int result = tasklistService.deleteTasklistById(tasklistId);
        taskService.resetTasklistIdForTasks(tasklistId);//将所有这个tasklistId的任务的tasklistId设置成0
        if (result == 1) {
            return Result.succ(null);
        } else {
            return Result.fail("删除任务列表失败");
        }
    }


}