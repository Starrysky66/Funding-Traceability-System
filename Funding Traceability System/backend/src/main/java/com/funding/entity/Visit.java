package com.funding.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 回访记录实体
 */
@Data
@TableName("visit")
public class Visit {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long studentId;

    private Long sponsorId;

    private LocalDate visitDate;

    private String content;

    private String situation;

    private String problem;

    private String visitor;

    private LocalDateTime createTime;
}
