package com.funding.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.funding.common.BusinessException;
import com.funding.common.JwtUtils;
import com.funding.common.UserContext;
import com.funding.entity.SysUser;
import com.funding.mapper.SysUserMapper;
import com.funding.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 认证服务实现
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public String login(String username, String password) {
        SysUser user = sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("username", username));
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        // Demo简化：明文密码直接比较
        if (password == null || !password.equals(user.getPassword())) {
            throw new BusinessException("密码错误");
        }
        return JwtUtils.generate(user.getId(), user.getUsername(), user.getRole());
    }

    @Override
    public SysUser info() {
        Long userId = UserContext.getUserId();
        SysUser user = sysUserMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(401, "用户不存在");
        }
        // 不返回密码
        user.setPassword(null);
        return user;
    }
}
