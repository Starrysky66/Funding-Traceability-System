package com.funding.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 评分指标实体
 */
@Data
@TableName("score_item")
public class ScoreItem {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private BigDecimal weight;

    private String description;
}
