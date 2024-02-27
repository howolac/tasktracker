package com.tasktracker.mapper;

import com.tasktracker.entity.Count;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author lzx
 * @since 2023-12-28
 */
@Mapper
public interface CountMapper extends BaseMapper<Count> {
    void insertCount(Count count);

    List<Count> selectThisWeekCounts(@Param("userId") int userId);

    List<Map<String, Object>> selectMonthlySummaryCounts(@Param("userId") int userId);

    void removeTempCountToday(int userId);
}
