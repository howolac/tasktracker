package com.tasktracker.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tasktracker.entity.Tasklist;
import com.tasktracker.mapper.TasklistMapper;
import com.tasktracker.service.TasklistService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tasktracker.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lzx
 * @since 2023-12-09
 */
@Service
public class TasklistServiceImpl extends ServiceImpl<TasklistMapper, Tasklist> implements TasklistService {
    private final TasklistMapper tasklistMapper;

    @Autowired
    public TasklistServiceImpl(TasklistMapper tasklistMapper) {
        this.tasklistMapper = tasklistMapper;
    }

    @Override
    public int deleteTasklistById(int tasklistId) {
        return tasklistMapper.deleteTasklistById(tasklistId);
    }

    @Override
    public List<Tasklist> getAllTasklistsByUserId(int userId) {
        return tasklistMapper.selectAllTasklistsByUserId(userId);
    }

    @Override
    public int insertTasklist(Tasklist tasklist) {
        return tasklistMapper.insertTasklist(tasklist);
    }

    @Override
    public int updateTasklist(Tasklist tasklist) {
        return tasklistMapper.updateTasklist(tasklist);
    }

    @Override
    public IPage<Tasklist> searchTasklistsByOptionalText(Page<Tasklist> page, Integer userId, String searchNameText, String searchNoteText) {
        return tasklistMapper.searchTasklistsByOptionalText(page, userId, searchNameText, searchNoteText);
    }


}
