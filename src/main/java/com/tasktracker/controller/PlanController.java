package com.tasktracker.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tasktracker.common.lang.Result;
import com.tasktracker.entity.Goal;
import com.tasktracker.entity.Plan;
import com.tasktracker.service.PlanService;
import com.tasktracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/plans")
public class PlanController {

    private final PlanService planService;
    @Autowired
    private UserService userService;

    @Autowired
    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    @GetMapping("/search")
    public Result findAllPlansPage(
            @RequestParam int page, @RequestParam int limit,
            @RequestParam(value = "searchNameText", required = false) String searchNameText,
            @RequestParam(value = "searchFrequencyText", required = false) String searchFrequencyText
    ) {
        Page<Plan> pageReq = new Page<>(page, limit); // 第page页，每页limit条数据
        IPage<Plan> planPage = planService.search(pageReq, userService.getCurrentUserId(), searchNameText, searchFrequencyText);
        return Result.succ(planPage);
    }

    @GetMapping("/getAll")
    public Result findAllPlans() {
        List<Plan> plans = planService.findAll(userService.getCurrentUserId());
        List<Plan> updatedPlans = new ArrayList<>();
        for (Plan plan : plans) {
            Plan updatedPlan = planService.getUpdatedExecutionPlan(plan);
            updatedPlans.add(updatedPlan);
        }
        System.out.println(updatedPlans);
        return Result.succ(updatedPlans);
    }

    @GetMapping("/getToday")
    public Result getPlansToday() {
        List<Plan> plans = planService.findPlansForTodayByUserId(userService.getCurrentUserId());
        return Result.succ(plans);
    }

    // 创建新计划
    @PostMapping("/insert")
    public Result insertPlan(@RequestBody Plan plan) {
        plan.setUserId(userService.getCurrentUserId());
        planService.insertPlan(plan);
        return Result.succ(null);
    }

    // 更新指定ID的计划
    @PutMapping("/update")
    public Result updatePlan(@RequestBody Plan plan) {
        planService.update(plan);
        return Result.succ(null);
    }

    // 删除指定ID的计划
    @DeleteMapping("delete/{planId}")
    public Result deletePlan(@PathVariable Integer planId) {
        planService.deleteById(planId);
        return Result.succ(null);
    }

    // 切换激活状态
    @PutMapping("/toggle-status/{planId}")
    public Result togglePlanStatus(@PathVariable Integer planId) {
        try {
            planService.togglePlanStatus(planId);
            return Result.succ(null);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    @PutMapping("/toggle-done/{planId}")
    public Result togglePlanDone(@PathVariable Integer planId) {
        try {
            planService.togglePlanDone(planId);
            return Result.succ(null);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }
}