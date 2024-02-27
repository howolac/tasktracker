package com.tasktracker.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tasktracker.entity.Goal;
import com.tasktracker.mapper.GoalMapper;
import com.tasktracker.service.GoalService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lzx
 * @since 2023-12-07
 */
@Service
public class GoalServiceImpl extends ServiceImpl<GoalMapper, Goal> implements GoalService {
    private final GoalMapper goalMapper;

    @Autowired
    public GoalServiceImpl(GoalMapper goalMapper) {
        this.goalMapper = goalMapper;
    }

    @Override
    public IPage<Goal> search(Page<Goal> page, Integer userId, String searchNameText, String searchContentText) {
        return goalMapper.search(page, userId, searchNameText, searchContentText);
    }

    @Override
    public void updateGoal(Goal goal) {
        goalMapper.updateGoal(goal);
    }

    @Override
    public void addGoal(Goal goal) {
        goalMapper.addGoal(goal);
    }

    @Override
    public void deleteGoal(Integer goalId) {
        goalMapper.deleteGoal(goalId);
    }
}
