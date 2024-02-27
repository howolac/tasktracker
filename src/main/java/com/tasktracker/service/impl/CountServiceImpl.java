package com.tasktracker.service.impl;

import com.tasktracker.entity.Count;
import com.tasktracker.mapper.CountMapper;
import com.tasktracker.service.CountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tasktracker.service.PlanService;
import com.tasktracker.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lzx
 * @since 2023-12-28
 */
@Service
public class CountServiceImpl extends ServiceImpl<CountMapper, Count> implements CountService {
    private final CountMapper countMapper;

    @Autowired
    TaskService taskService;
    @Autowired
    PlanService planService;


    @Autowired
    public CountServiceImpl(CountMapper countMapper) {
        this.countMapper = countMapper;
    }

    @Override
    public void insertCount(int userId) {
        removeTempCount(userId);//为了获取统计数据会事先插入
        Count count = getTodayCounts(userId);
        count.setUserId(userId);
        count.setCountCreate(LocalDateTime.now());
        count.setCountModified(LocalDateTime.now());
        countMapper.insertCount(count);
    }

    @Override
    public List<Count> selectThisWeekCounts(int userId) {
        return countMapper.selectThisWeekCounts(userId);
    }

    @Override
    public List<Map<String, Object>> selectMonthlySummaryCounts(int userId) {
        return countMapper.selectMonthlySummaryCounts(userId);
    }

    private void removeTempCount(int userId) {
        countMapper.removeTempCountToday(userId);
    }

    //       这里做个即时统计
    @Override
    public Count getTodayCounts(int userId) {
        Count count = new Count();
        count.setCountTask(taskService.countTodayCompletedTasksByUserId(userId));//今天完成的任务
        count.setCountTaskTotal(taskService.countTasksStartingTodayByUserId(userId));//开始时间是今天的任务
        count.setCountPlan(planService.countTodayCompletedPlansByUserId(userId));//今天已经完成的计划
        count.setCountPlanTotal(planService.countPlanNextPerformTodayByUserId(userId));//下次执行是今天的计划
        return count;
    }

}
