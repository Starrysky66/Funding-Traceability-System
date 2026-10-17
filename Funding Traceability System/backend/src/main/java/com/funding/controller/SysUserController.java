package com.funding.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.funding.common.AdminOnly;
import com.funding.common.Result;
import com.funding.entity.SysUser;
import com.funding.service.SysUserService;
import com.funding.vo.OptionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理接口（仅 ADMIN）
 */
@RestController
@RequestMapping("/api/users")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @GetMapping
    @AdminOnly
    public Result<IPage<SysUser>> page(@RequestParam(defaultValue = "1") Integer page,
                                       @RequestParam(defaultValue = "10") Integer size,
                                       @RequestParam(required = false) String keyword) {
        return Result.success(sysUserService.page(page, size, keyword));
    }

    @GetMapping("/options")
    public Result<List<OptionVO>> options(@RequestParam(required = false) String role) {
        return Result.success(sysUserService.options(role));
    }

    @PostMapping
    @AdminOnly
    public Result<Void> save(@RequestBody SysUser user) {
        sysUserService.save(user);
        return Result.success();
    }

    @PutMapping("/{id}")
    @AdminOnly
    public Result<Void> update(@PathVariable Long id, @RequestBody SysUser user) {
        user.setId(id);
        sysUserService.update(user);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @AdminOnly
    public Result<Void> delete(@PathVariable Long id) {
        sysUserService.delete(id);
        return Result.success();
    }
}
