package com.tasktracker.service;

import com.tasktracker.entity.Count;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author lzx
 * @since 2023-12-28
 */
public interface CountService extends IService<Count> {
    void insertCount(int userId);

    List<Count> selectThisWeekCounts(@Param("userId") int userId);

    List<Map<String, Object>> selectMonthlySummaryCounts(@Param("userId") int userId);

    Count getTodayCounts(int userId);

}
