package com.funding.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.funding.entity.SysUser;
import com.funding.vo.OptionVO;

import java.util.List;

/**
 * 系统用户服务
 */
public interface SysUserService {

    IPage<SysUser> page(Integer page, Integer size, String keyword);

    void save(SysUser user);

    void update(SysUser user);

    void delete(Long id);

    /**
     * 下拉选项（按角色过滤）
     */
    List<OptionVO> options(String role);
}
