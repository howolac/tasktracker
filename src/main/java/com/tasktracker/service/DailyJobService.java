package com.tasktracker.service;

import com.tasktracker.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class DailyJobService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private PlanService planService;
    @Autowired
    private CountService countService;

    @Autowired
    private MailService mailService;


    //    统计plan完成率的数据
    @Scheduled(cron = "50 59 23 * * ?") // 每天晚上23:59执行
//    @Scheduled(cron = "0 47 2 * * ?")
    public void updateAllUserPlanExecutionCounts() {
        List<User> users = userService.selectAllUser();
        users.forEach(user -> {
            try {
                planService.updateAllPlanExecutionCountsByUserId(user.getUserId());
            } catch (Exception e) {
            }
        });
    }

    //    统计当天完成的任务和计划任务情况
    @Scheduled(cron = "50 59 23 * * ?") // 每天晚上23:59:50执行，
    public void countAndUpdateAllUsersCountDaily() {
        List<User> users = userService.selectAllUser();
        for (User user : users) {
            countService.insertCount(user.getUserId());
        }

    }

    //    因为下次执行时间至少是一天后，所以定时检查过期和更新
    @Scheduled(cron = "50 59 23 * * ?") // 每天晚上23:59执行
    public void updateNextExecutionTimeForActivePlans() {
        List<User> users = userService.selectAllUser();
        users.forEach(user -> {
            try {
                planService.updateNextExecutionTimeForActivePlans(user.getUserId());
            } catch (Exception e) {
            }
        });
    }
    //    重置记录plan的planDone 和 task的每日完成情况的_Done属性
    @Scheduled(cron = "59 59 23 * * ?") // 每天晚上23:59执行
    public void resetAllUsersCountDaily() {
        List<User> users = userService.selectAllUser();
        users.forEach(user -> {
            try {
                planService.resetCompletedPlansByUserId(user.getUserId());
                taskService.resetCompletedTasksByUserId(user.getUserId());
            } catch (Exception e) {
            }
        });
    }

    @Scheduled(cron = "0 0 7 * * ?")// 每天上午7点执行的定时任务
//    @Scheduled(cron = "*/45 * * * * ?") // 每隔45秒执行的定时任务
    public void sendScheduledEmails() {
        List<User> users = userService.selectAllUser(); // 获取所有用户
        for (User user : users) {
            String userEmail = user.getUserEmail();
            int userIsEmailEnabled = user.getUserIsEmailEnabled();//用户是否开启了邮件提醒功能
            if (userIsEmailEnabled != 0 && StringUtils.hasText(userEmail) && isValidEmail(userEmail)) {
                logger.info("用户 {} 的邮件开始发送:", user.getUserId());
                Integer currentUserId = user.getUserId();
                String emailContent = prepareEmailTaskContent(currentUserId);
                MailVo mailVo = new MailVo();
                mailVo.setFrom("q858103451@163.com");
                mailVo.setTo(userEmail);
                mailVo.setSubject("今天的任务和截止事项");
                mailVo.setText(emailContent); // 设置邮件内容
                mailService.sendMail(mailVo); // 发送邮件
            } else {
                logger.error("用户 {} 的邮箱为空或格式不正确: ", user.getUserId(), userEmail);
            }
        }
    }

    // 验证邮箱格式是否正确
    private boolean isValidEmail(String email) {
        // 邮箱格式验证的正则表达式
        Pattern emailPattern =
                Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
        return emailPattern.matcher(email).matches();
    }

    private String prepareEmailTaskContent(Integer currentUserId) {
        List<Task> tasksStartingToday = taskService.findTasksStartingTodayOrderedByStartTimeByUserId(currentUserId);
        List<Task> todayDeadlineTasks = taskService.findTodayDeadlineTasksByUserId(currentUserId);
        List<Plan> plansToday = planService.findPlansForTodayByUserId(currentUserId);

        List<Map<String, Object>> combinedItems = new ArrayList<>();

        // 将任务添加到列表
        for (Task task : tasksStartingToday) {
            Map<String, Object> item = new HashMap<>();
            item.put("dateTime", task.getTaskStart());
            item.put("name", task.getTaskName());
            combinedItems.add(item);
        }

        // 将计划添加到列表
        for (Plan plan : plansToday) {
            Map<String, Object> item = new HashMap<>();
            item.put("dateTime", plan.getPlanNextPerform());
            item.put("name", plan.getPlanName());
            combinedItems.add(item);
        }

        // 排序合并后的列表
        combinedItems.sort((item1, item2) -> {
            LocalDateTime dateTime1 = (LocalDateTime) item1.get("dateTime");
            LocalDateTime dateTime2 = (LocalDateTime) item2.get("dateTime");
            return dateTime1.compareTo(dateTime2);
        });

        StringBuilder emailContentBuilder = new StringBuilder();


        // 格式化今天待办的任务
        if (!combinedItems.isEmpty()) {
            emailContentBuilder.append("今日待办:\n");
            for (Map<String, Object> item : combinedItems) {
                LocalDateTime dateTime = (LocalDateTime) item.get("dateTime");
                String formattedTime = formatTime(dateTime.toString());
                emailContentBuilder.append(formattedTime).append("  ").append(item.get("name")).append("\n");
            }
        } else {
            emailContentBuilder.append("今天没有待办任务哦。\n");
        }

        emailContentBuilder.append("\n");

        // 格式化今天截止的任务
        if (!todayDeadlineTasks.isEmpty()) {
            emailContentBuilder.append("今日截止事项:\n");
            for (Task task : todayDeadlineTasks) {
                LocalDateTime deadline = task.getTaskDeadline();
                String formattedDeadline = formatTime(deadline.toString());
                emailContentBuilder.append(formattedDeadline).append("  ").append(task.getTaskName()).append("\n");
            }
        } else {
            emailContentBuilder.append("今天没有截止的任务哦。\n");
        }

        return emailContentBuilder.toString();
    }

    private String formatTime(String dateTime) {
        String time = dateTime.split("T")[1].split("\\.")[0]; // 获取时间部分
        // 如果时间格式是 "xx:xx:00"，则去除末尾的 ":00"
        if (time.matches("\\d{2}:\\d{2}:\\d{2}")) {
            time = time.substring(0, time.length() - 3);
        }
        return time;
    }


}
