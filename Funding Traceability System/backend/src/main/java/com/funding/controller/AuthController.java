package com.funding.controller;

import com.funding.common.Result;
import com.funding.entity.SysUser;
import com.funding.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

/**
 * 认证接口
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 登录，返回 {token}
     */
    @PostMapping("/login")
    public Result<Map<String, String>> login(@RequestBody Map<String, String> body) {
        String token = authService.login(body.get("username"), body.get("password"));
        return Result.success(Collections.singletonMap("token", token));
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/info")
    public Result<SysUser> info() {
        return Result.success(authService.info());
    }

    /**
     * 登出（Demo 无服务端状态，直接返回成功）
     */
    @PostMapping("/logout")
    public Result<Void> logout() {
        return Result.success();
    }
}
