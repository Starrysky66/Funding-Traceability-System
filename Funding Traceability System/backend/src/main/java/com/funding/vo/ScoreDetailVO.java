package com.funding.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 评分明细返回对象（含指标名称、权重）
 */
@Data
public class ScoreDetailVO {

    private Long id;

    private Long scoreId;

    private Long itemId;

    private String itemName;

    private BigDecimal weight;

    private BigDecimal itemScore;
}
