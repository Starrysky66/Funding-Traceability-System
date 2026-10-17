package com.funding.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 资金流水实体
 */
@Data
@TableName("fund")
public class Fund {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long studentId;

    private Long sponsorId;

    private BigDecimal amount;

    private LocalDate fundDate;

    private String purpose;

    private String remark;

    private LocalDateTime createTime;
}
