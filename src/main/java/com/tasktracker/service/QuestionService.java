package com.tasktracker.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tasktracker.entity.Question;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author lzx
 * @since 2023-12-13
 */
public interface QuestionService extends IService<Question> {
    int deleteById(Integer question);

    IPage<Question> search(Page<Question> page, int userId, String searchNameText, String searchCircumstancesText);

    int insert(Question question);

    int update(Question question);

    void toggleQuesStatusById(Integer question);

}
