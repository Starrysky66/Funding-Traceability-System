package com.funding.service;

import com.funding.entity.SysUser;

/**
 * 认证服务
 */
public interface AuthService {

    /**
     * 登录，返回 token
     */
    String login(String username, String password);

    /**
     * 获取当前登录用户信息
     */
    SysUser info();
}
