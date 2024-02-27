package com.tasktracker.security.handlers;

import com.tasktracker.common.lang.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        // 打印日志或进行其他记录
        System.out.println("Authentication failed for user: " + request.getParameter("username") + " - " + exception.getMessage());

        // 设置响应的状态码为 401
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // 设置响应的内容类型为 JSON
        response.setContentType("application/json;charset=UTF-8");

        // 使用Result类生成失败响应数据
        Result result = Result.fail(HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage(), null);

        // 将Result对象写入响应体
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}