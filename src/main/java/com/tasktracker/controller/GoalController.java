package com.tasktracker.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tasktracker.common.lang.Result;
import com.tasktracker.entity.Goal;
import com.tasktracker.entity.Plan;
import com.tasktracker.service.GoalService;
import com.tasktracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
public class GoalController {
    private final GoalService goalService;
    @Autowired
    private UserService userService;

    @Autowired
    public GoalController(GoalService goalService, UserService userService) {
        this.goalService = goalService;
    }

    @GetMapping("/search")
    public Result findGoalsByUserId(
            @RequestParam int page, @RequestParam int limit,
            @RequestParam(value = "searchNameText", required = false) String searchNameText,
            @RequestParam(value = "searchContentText", required = false) String searchContentText
    ) {
        Page<Goal> pageReq = new Page<>(page, limit); // 第page页，每页limit条数据
        IPage<Goal> goalPage = goalService.search(pageReq, userService.getCurrentUserId(), searchNameText, searchContentText);
        return Result.succ(goalPage);
    }



    @PostMapping("/add")
    public Result addGoal(@RequestBody Goal goal) {
        goal.setUserId(userService.getCurrentUserId());
        goalService.addGoal(goal);
        return Result.succ(null);
    }

    @PutMapping("/update")
    public Result updateGoal(@RequestBody Goal goal) {
        goalService.updateGoal(goal);
        return Result.succ(null);
    }

    @DeleteMapping("delete/{goalId}")
    public Result deleteGoal(@PathVariable("goalId") Integer goalId) {
        goalService.deleteGoal(goalId);
        return Result.succ(null);
    }
}