package com.funding.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 学生实体
 */
@Data
@TableName("student")
public class Student {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String gender;

    private String idCard;

    private String school;

    private String grade;

    private String familyAddress;

    private String familySituation;

    private String phone;

    private Long sponsorId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
