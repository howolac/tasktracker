package com.tasktracker.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tasktracker.entity.Plan;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author lzx
 * @since 2023-12-09
 */
public interface PlanService extends IService<Plan> {

    List<Plan> findAll(Integer userId);

    IPage<Plan> search(Page<Plan> page, Integer userId, String searchNameText, String searchFrequencyText);

    void insertPlan(Plan plan);

    void deleteById(Integer planId);

    List<Plan> findPlansForTodayByUserId(Integer userId);

    //    更新
    void update(Plan plan);

    Plan getUpdatedExecutionPlan(Plan plan);

    void togglePlanDone(Integer planId);

    void togglePlanStatus(Integer planId);

    void updateNextExecutionTimeForActivePlans(Integer userId);
//    统计

    Integer countTodayCompletedPlansByUserId(Integer userId);

    int countPlanNextPerformTodayByUserId(@Param("userId") int userId);

    void resetCompletedPlansByUserId(Integer userId);

    void updateAllPlanExecutionCountsByUserId(Integer userId);

}
