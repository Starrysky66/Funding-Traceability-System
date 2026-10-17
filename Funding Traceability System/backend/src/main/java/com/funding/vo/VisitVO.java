package com.funding.vo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 回访记录查询返回对象（含学生姓名、资助人姓名）
 */
@Data
public class VisitVO {

    private Long id;

    private Long studentId;

    private Long sponsorId;

    private LocalDate visitDate;

    private String content;

    private String situation;

    private String problem;

    private String visitor;

    private LocalDateTime createTime;

    private String studentName;

    private String sponsorName;
}
