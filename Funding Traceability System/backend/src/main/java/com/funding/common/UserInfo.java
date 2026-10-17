package com.funding.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 当前登录用户信息（从JWT解析）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    private Long userId;

    private String username;

    private String role;
}
