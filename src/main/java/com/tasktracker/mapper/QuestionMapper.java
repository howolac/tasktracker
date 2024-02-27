package com.tasktracker.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tasktracker.entity.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tasktracker.entity.Tasklist;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author lzx
 * @since 2023-12-13
 */
@Mapper
public interface QuestionMapper extends BaseMapper<Question> {
    int deleteById(Integer questionId);

    List<Question> findAllByUserId(Integer userId);

    IPage<Question> search(Page<Question> page, int userId, String searchNameText, String searchCircumstancesText);

    int insert(Question question);

    int update(Question question);

    void toggleQuesStatusById(Integer questionId);
}
