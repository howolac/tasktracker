package com.tasktracker.controller;

import com.tasktracker.common.lang.Result;
import com.tasktracker.entity.Count;
import com.tasktracker.service.CountService;
import com.tasktracker.service.PlanService;
import com.tasktracker.service.TaskService;
import com.tasktracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/counts")
public class CountController {
    private final CountService countService;

    @Autowired
    private PlanService planService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;

    @Autowired
    public CountController(CountService countService) {
        this.countService = countService;
    }

    // 获取今天的统计数据
    @GetMapping("/today")
    public Result getTodayCounts() {
        countService.insertCount(userService.getCurrentUserId());
        return Result.succ(countService.getTodayCounts(userService.getCurrentUserId()));
    }

    // 获取本周的统计数据
    @GetMapping("/thisWeek")
    public Result getThisWeekCounts() {
        List<Count> data = countService.selectThisWeekCounts(userService.getCurrentUserId());
        return Result.succ(data);
    }

    // 获取每个月的统计数据汇总
    @GetMapping("/monthlySummary")
    public Result getMonthlySummaryCounts() {
        List<Map<String, Object>> data = countService.selectMonthlySummaryCounts(userService.getCurrentUserId());
        return Result.succ(data);
    }
}
