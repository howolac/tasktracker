package com.tasktracker.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tasktracker.entity.Plan;
import com.tasktracker.mapper.PlanMapper;
import com.tasktracker.service.PlanService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
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
public class PlanServiceImpl extends ServiceImpl<PlanMapper, Plan> implements PlanService {
    private final PlanMapper planMapper;

    @Autowired
    public PlanServiceImpl(PlanMapper planMapper) {
        this.planMapper = planMapper;

    }

    @Override
    public List<Plan> findAll(Integer userId) {
        QueryWrapper<Plan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .orderByDesc("plan_status");
        return planMapper.selectList(queryWrapper);
    }

    @Override
    public IPage<Plan> search(Page<Plan> page, Integer userId, String searchNameText, String searchFrequencyText) {
        QueryWrapper<Plan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .orderByDesc("plan_status");

        // 如果 searchFrequencyText 不为空，则添加筛选条件
        if (searchFrequencyText != null && !searchFrequencyText.isEmpty()) {
            queryWrapper.like("plan_frequency", searchFrequencyText);
        }

        // 如果 searchNameText 不为空，则添加筛选条件
        if (searchNameText != null && !searchNameText.isEmpty()) {
            queryWrapper.like("plan_name", searchNameText);
        }

        return planMapper.selectPage(page, queryWrapper);
    }

    @Override
    public void insertPlan(Plan plan) {
        if (plan.getPlanStatus() == 1) {//如果是激活状态，更新下次执行时间
            plan.setPlanNextPerform(getPlanNextExecutionTime(plan));
        }
        planMapper.insertPlan(plan);
    }

    @Override
    public void deleteById(Integer planId) {
        planMapper.deleteById(planId);
    }

    @Override
    public void update(Plan plan) {
        planMapper.update(plan);
    }

    @Override
    public void togglePlanStatus(Integer planId) {
        Plan plan = planMapper.selectById(planId);
        if (plan.getPlanStatus() == 0) {//如果改成激活状态，更新下次执行时间
            plan.setPlanStatus(1);
            plan.setPlanNextPerform(getPlanNextExecutionTime(plan));
        } else if (plan.getPlanStatus() == 1) {
            plan.setPlanStatus(0);
            plan.setPlanNextPerform(null);
        }
        update(plan);
    }

    @Override
    public Integer countTodayCompletedPlansByUserId(Integer userId) {
        return planMapper.countTodayCompletedPlansByUserId(userId);// 统计完成的计划数量
    }

    @Override
    public int countPlanNextPerformTodayByUserId(int userId) {
        return planMapper.countPlanNextPerformTodayByUserId(userId);
    }

    @Override
    public void resetCompletedPlansByUserId(Integer userId) {
        planMapper.resetCompletedPlansByUserId(userId);
    }

    @Override
    public void updateAllPlanExecutionCountsByUserId(Integer userId) {
        List<Plan> plans = findAll(userId);
        for (Plan plan : plans) {
            update(getUpdatedExecutionPlan(plan));
        }
    }

    @Override
    public Plan getUpdatedExecutionPlan(Plan plan) {
        LocalDate today = LocalDate.now();
        if (plan.getPlanNextPerform() != null && plan.getPlanNextPerform().toLocalDate().isEqual(today)) {  // 检查计划的下次执行时间是否为今天
            Integer totalPlansCount = plan.getTotalPlansCount() != null ? plan.getTotalPlansCount() : 0;
            plan.setTotalPlansCount(totalPlansCount + 1);//总执行数加1

            if (plan.getPlanDone() != null && plan.getPlanDone() == 1) {//今天的完成情况是不是1
                Integer executedPlansCount = plan.getExecutedPlansCount() != null ? plan.getExecutedPlansCount() : 0;
                plan.setExecutedPlansCount(executedPlansCount + 1);//用户执行的次数加1
            }

        }
        return plan;
    }

    @Override
    public void togglePlanDone(Integer planId) {
        Plan plan = planMapper.selectById(planId);
        plan.setPlanDone(plan.getPlanDone() == 0 ? 1 : 0);
        update(plan);
    }

    @Override
    public List<Plan> findPlansForTodayByUserId(Integer userId) {
        return planMapper.findPlansForTodayByUserId(userId);
    }

    // 查找并更新激活的、下次执行时间在今天之前的过期的计划
    public void updateNextExecutionTimeForActivePlans(Integer userId) {
        List<Plan> plans = planMapper.findAllWithStatusOneAndNextPerformBeforeToday(userId);
        for (Plan plan : plans) {
            if (plan == null) {
                continue;
            }
            if (plan.getPlanNextPerform() != null) {// 如果原先的下次执行时间不为空，则将其设置为上次执行时间

                plan.setPlanLastPerform(plan.getPlanNextPerform());
            }

            plan.setPlanNextPerform(getPlanNextExecutionTime(plan));// 计算新的下次执行时间，并设置

            // 更新计划的信息到数据库（这行代码假设存在一个方法来更新数据库中的计划）
            planMapper.update(plan);
        }
    }

    private LocalDateTime getPlanNextExecutionTime(Plan plan) {
        if (plan == null) {
            return null;
        }

        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime nextExecutionTime = currentDateTime;

        // 根据执行频率计算下次执行时间
        switch (plan.getPlanFrequency()) {
            case "daily":
                nextExecutionTime = getNextExecutionTimeDaily(plan, currentDateTime);
                break;
            case "weekly":
                nextExecutionTime = getNextExecutionTimeWeekly(plan, currentDateTime);
                break;
            case "monthly":
                nextExecutionTime = getNextExecutionTimeMonthly(plan, currentDateTime);
                break;
            default:
                throw new IllegalArgumentException("不支持的执行频率: " + plan.getPlanFrequency());
        }

        return nextExecutionTime;
    }

    private LocalDateTime getNextExecutionTimeDaily(Plan plan, LocalDateTime currentDateTime) {
        LocalTime executionTime = plan.getPlanTime();
        if (executionTime == null) {//不能为空
            throw new IllegalStateException("每日计划必须设置执行时间。");
        }
        LocalDateTime nextExecutionTime = currentDateTime.toLocalDate().atTime(executionTime);

        if (nextExecutionTime.isBefore(currentDateTime)) {
            nextExecutionTime = nextExecutionTime.plusDays(1);
        }
        return nextExecutionTime;
    }

    private LocalDateTime getNextExecutionTimeWeekly(Plan plan, LocalDateTime currentDateTime) {
//        List<String> daysOfWeek = Arrays.asList(plan.getPlanDayOfWeek().split("\\s+"));
        List<String> daysOfWeek = plan.getPlanDayOfWeek() != null ? Arrays.asList(plan.getPlanDayOfWeek().split("\\s+")) : null;
        LocalTime executionTime = plan.getPlanTime(); // 直接获取LocalTime对象
        if (daysOfWeek == null || daysOfWeek.isEmpty() || executionTime == null) {
            throw new IllegalStateException("每周计划必须设置星期几和执行时间。");
        }
        LocalDateTime nextExecutionTime = currentDateTime;

        for (String day : daysOfWeek) {
            DayOfWeek dayOfWeek = DayOfWeek.of(Integer.parseInt(day));
            nextExecutionTime = currentDateTime.with(TemporalAdjusters.nextOrSame(dayOfWeek))
                    .toLocalDate()
                    .atTime(executionTime);

            if (!nextExecutionTime.isBefore(currentDateTime)) {
                break;
            }
        }

        return nextExecutionTime;
    }

    private LocalDateTime getNextExecutionTimeMonthly(Plan plan, LocalDateTime currentDateTime) {
//        List<String> daysOfMonth = Arrays.asList(plan.getPlanMonthDay().split("\\s+"));
        List<String> daysOfMonth = plan.getPlanMonthDay() != null ? Arrays.asList(plan.getPlanMonthDay().split("\\s+")) : null;
        LocalTime executionTime = plan.getPlanTime();
        if (daysOfMonth == null || daysOfMonth.isEmpty() || executionTime == null) {
            throw new IllegalStateException("每月计划必须设置具体日期和执行时间。");
        }
        LocalDateTime nextExecutionTime = currentDateTime;
        int month = currentDateTime.getMonthValue();

        for (String day : daysOfMonth) {
            int dayOfMonth = Integer.parseInt(day);
            nextExecutionTime = LocalDate.of(currentDateTime.getYear(), month, dayOfMonth)
                    .atTime(executionTime);

            if (!nextExecutionTime.isBefore(currentDateTime)) {
                break;
            }
        }

        // 如果计算出的时间早于当前时间，说明这个月的执行日已经过去，要计算下个月的
        if (nextExecutionTime.isBefore(currentDateTime)) {
            month = (month % 12) + 1; // 进入下一个月
            for (String day : daysOfMonth) {
                int dayOfMonth = Integer.parseInt(day);
                nextExecutionTime = LocalDate.of(currentDateTime.getYear(), month, dayOfMonth)
                        .atTime(executionTime);
                // 只需要找到下个月的第一个执行日
                if (!nextExecutionTime.isBefore(currentDateTime)) {
                    break;
                }
            }
        }

        return nextExecutionTime;
    }

}
