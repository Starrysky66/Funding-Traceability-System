package com.funding.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 仪表盘统计返回对象
 */
@Data
public class DashboardVO {

    private Long studentCount;

    private BigDecimal fundTotalAmount;

    private Long fundCount;

    private Long visitCount;

    private Long scoreCount;

    private ModuleBar moduleBar;

    private List<MonthlyFund> monthlyFunds;

    private List<PurposeDist> purposeDistribution;

    private List<ScoreAvg> scoreAverages;

    @Data
    public static class ModuleBar {
        private Long student;
        private Long fund;
        private Long visit;
        private Long score;
    }

    @Data
    public static class MonthlyFund {
        private String month;
        private BigDecimal amount;
    }

    @Data
    public static class PurposeDist {
        private String name;
        private Long value;
    }

    @Data
    public static class ScoreAvg {
        private String itemName;
        private BigDecimal avg;
    }
}
