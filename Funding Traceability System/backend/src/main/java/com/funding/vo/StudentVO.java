package com.funding.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 学生查询返回对象（含资助人姓名）
 */
@Data
public class StudentVO {

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

    private String sponsorName;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
