package com.funding.common;

/**
 * 当前登录用户上下文（基于ThreadLocal）
 * 在 JwtInterceptor 中解析token后 set，请求结束 clear
 */
public class UserContext {

    private static final ThreadLocal<UserInfo> CONTEXT = new ThreadLocal<>();

    public static void set(UserInfo userInfo) {
        CONTEXT.set(userInfo);
    }

    public static UserInfo get() {
        return CONTEXT.get();
    }

    public static Long getUserId() {
        UserInfo info = CONTEXT.get();
        return info == null ? null : info.getUserId();
    }

    public static String getRole() {
        UserInfo info = CONTEXT.get();
        return info == null ? null : info.getRole();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
