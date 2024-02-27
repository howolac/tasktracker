package com.tasktracker.security.handlers;

import com.tasktracker.common.lang.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tasktracker.entity.CustomUserDetails;
import com.tasktracker.security.jwt.JWTUtils;
import com.tasktracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserService userService; // 注入UserService

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        // 检查principal是否是CustomUserDetails的实例
        if (authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            // 从CustomUserDetails获取用户名和权限列表
            String username = customUserDetails.getUsername();
            List<String> authorities = customUserDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            Integer userId = customUserDetails.getUserId();


            // 更新用户的最后登录时间
            userService.updateUserLastLoginTime(userId); // 调用UserService更新最后登录时间

            //        // 设置响应的状态码
////        response.setStatus(HttpServletResponse.SC_OK);

            // 生成JWT令牌 ，并将JWT令牌添加到响应头
            String token = jwtUtils.generateToken(username, authorities);
            response.setHeader("Authorization", "Bearer " + token);

            // 设置响应内容类型为JSON,响应的字符编码为UTF-8
            response.setContentType("application/json;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");

            // 构造一个结果对象
            Map<String, Object> data = new HashMap<>();
            data.put("userId", userId);
            data.put("username", username);
            data.put("authorities", authorities);
            Result result = Result.succ(data);
            // 将Result对象写入响应体
            response.getWriter().write(objectMapper.writeValueAsString(result));

            // 记录认证成功的信息
            System.out.println("用户: " + username + " 认证成功。用户ID: " + userId);
        } else {
            // 如果principal不是CustomUserDetails实例，需要处理这种情况
            // 可能是抛出异常或者返回错误信息
        }
    }
}