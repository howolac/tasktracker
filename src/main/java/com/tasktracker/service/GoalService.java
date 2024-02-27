package com.tasktracker.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tasktracker.entity.Goal;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tasktracker.entity.Plan;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lzx
 * @since 2023-12-07
 */
public interface GoalService extends IService<Goal> {
    IPage<Goal> search(Page<Goal> page, Integer userId, String searchNameText, String searchContentText);
    void updateGoal(Goal goal);
    void addGoal(Goal goal);
    void deleteGoal(Integer goalId);
}
