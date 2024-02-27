package com.tasktracker.mapper;

import com.tasktracker.entity.Plan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author lzx
 * @since 2023-12-09
 */
@Mapper
public interface PlanMapper extends BaseMapper<Plan> {

    void insertPlan(Plan plan);

    void update(Plan plan);

    List<Plan> findAllWithStatusOneAndNextPerformBeforeToday(Integer userId);

    List<Plan> findPlansForTodayByUserId(@Param("userId") Integer userId);

    int countTodayCompletedPlansByUserId(@Param("userId") int userId);

    int countPlanNextPerformTodayByUserId(@Param("userId") int userId);
    void resetCompletedPlansByUserId(@Param("userId") int userId);
}
