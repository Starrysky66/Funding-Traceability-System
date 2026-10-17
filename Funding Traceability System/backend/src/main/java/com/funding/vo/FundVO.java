package com.funding.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 资金流水查询返回对象（含学生姓名、资助人姓名）
 */
@Data
public class FundVO {

    private Long id;

    private Long studentId;

    private Long sponsorId;

    private BigDecimal amount;

    private LocalDate fundDate;

    private String purpose;

    private String remark;

    private LocalDateTime createTime;

    private String studentName;

    private String sponsorName;
}
