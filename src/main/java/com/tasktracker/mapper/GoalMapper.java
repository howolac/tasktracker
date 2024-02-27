package com.tasktracker.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tasktracker.entity.Goal;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author lzx
 * @since 2023-12-07
 */
@Mapper
public interface GoalMapper extends BaseMapper<Goal> {
    IPage<Goal> search(Page<Goal> page, Integer userId, String searchNameText, String searchContentText);

    void updateGoal(Goal goal);

    void addGoal(Goal goal);

    void deleteGoal(Integer goalId);
}
