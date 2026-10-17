package com.funding.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 角色权限拦截器
 * 检查处理方法或其所在类是否标注 @AdminOnly，若标注则要求当前用户为 ADMIN
 */
@Slf4j
@Component
public class RoleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        // 优先检查方法注解，再检查类注解
        AdminOnly adminOnly = handlerMethod.getMethodAnnotation(AdminOnly.class);
        if (adminOnly == null) {
            adminOnly = handlerMethod.getBeanType().getAnnotation(AdminOnly.class);
        }
        if (adminOnly == null) {
            return true;
        }
        // 需要 ADMIN 权限
        UserInfo user = UserContext.get();
        if (user == null || !"ADMIN".equals(user.getRole())) {
            writeForbidden(response);
            return false;
        }
        return true;
    }

    private void writeForbidden(HttpServletResponse response) throws Exception {
        response.setStatus(200);
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.write("{\"code\":403,\"message\":\"权限不足\",\"data\":null}");
            writer.flush();
        }
    }
}
