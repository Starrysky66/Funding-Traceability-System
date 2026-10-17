package com.funding.common;

import lombok.Data;

/**
 * 统一响应结果
 * {code:200, message:"success", data:T}
 */
@Data
public class Result<T> {

    private Integer code;

    private String message;

    private T data;

    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>();
        r.setCode(200);
        r.setMessage("success");
        r.setData(data);
        return r;
    }

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> error(Integer code, String message) {
        Result<T> r = new Result<>();
        r.setCode(code);
        r.setMessage(message);
        return r;
    }

    public static <T> Result<T> error(String message) {
        return error(500, message);
    }

    public static <T> Result<T> forbidden() {
        return error(403, "权限不足");
    }

    public static <T> Result<T> unauthorized() {
        return error(401, "未登录或Token失效");
    }
}
