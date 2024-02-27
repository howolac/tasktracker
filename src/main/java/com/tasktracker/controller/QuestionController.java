package com.tasktracker.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tasktracker.common.lang.Result;
import com.tasktracker.entity.Goal;
import com.tasktracker.entity.Question;
import com.tasktracker.service.QuestionService;
import com.tasktracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;
    @Autowired
    private UserService userService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }


    // 获取所有问题
    @GetMapping("/search")
    public Result findByUserId(@RequestParam int page, @RequestParam int limit, @RequestParam(value = "searchNameText", required = false) String searchNameText,
                               @RequestParam(value = "searchCircumstancesText", required = false) String searchCircumstancesText) {
        Page<Question> pageReq = new Page<>(page, limit); // 第page页，每页limit条数据
        IPage<Question> questionPage = questionService.search(pageReq, userService.getCurrentUserId(), searchNameText, searchCircumstancesText);
        return Result.succ(questionPage);
    }

    // 添加问题记录
    @PostMapping("/add")
    public Result insert(@RequestBody Question question) {
        question.setUserId(userService.getCurrentUserId());
        int result = questionService.insert(question);
        if (result == 0) {
            return Result.fail("新增失败");
        }
        return Result.succ(question);
    }

    // 删除问题
    @DeleteMapping("delete/{questionId}")
    public Result deleteById(@PathVariable Integer questionId) {
        int result = questionService.deleteById(questionId);
        if (result == 0) {
            return Result.fail("删除失败，未找到资源");
        }
        return Result.succ(null);
    }

    // 更新问题
    @PutMapping("/update")
    public Result update(@RequestBody Question question) {
        questionService.update(question);
        return Result.succ(question);
    }

    @PostMapping("/toggle-status/{questionId}")
    public Result toggleQuesStatusById(@PathVariable Integer questionId) {
        try {
            questionService.toggleQuesStatusById(questionId);
            return Result.succ(null);
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }
}