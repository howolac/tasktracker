package com.tasktracker.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tasktracker.entity.Question;
import com.tasktracker.mapper.QuestionMapper;
import com.tasktracker.service.QuestionService;
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
 * @since 2023-12-13
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {
    private final QuestionMapper questionMapper;

    @Autowired
    public QuestionServiceImpl(QuestionMapper questionMapper) {
        this.questionMapper = questionMapper;
    }

    @Override
    public int deleteById(Integer questionId) {
        return questionMapper.deleteById(questionId);
    }

    public List<Question> findAllByUserId(Integer userId) {
        return questionMapper.findAllByUserId(userId);
    }

    @Override
    public IPage<Question> search(Page<Question> page, int userId, String searchNameText, String searchCircumstancesText) {
        return questionMapper.search(page, userId, searchNameText, searchCircumstancesText);
    }

    @Override
    public int insert(Question question) {
        return questionMapper.insert(question);
    }

    @Override
    public int update(Question question) {
        return questionMapper.update(question);
    }

    @Override
    public void toggleQuesStatusById(Integer questionId) {
        questionMapper.toggleQuesStatusById(questionId);
    }
}
