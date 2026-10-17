package com.funding.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.funding.common.UserContext;
import com.funding.entity.Fund;
import com.funding.entity.Score;
import com.funding.entity.Student;
import com.funding.entity.Visit;
import com.funding.mapper.FundMapper;
import com.funding.mapper.ScoreDetailMapper;
import com.funding.mapper.ScoreMapper;
import com.funding.mapper.StudentMapper;
import com.funding.mapper.VisitMapper;
import com.funding.service.DashboardService;
import com.funding.vo.DashboardVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 仪表盘统计服务实现
 * SPONSOR 调用时统计仅基于自己名下数据（按 sponsor_id 过滤）
 */
@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private FundMapper fundMapper;
    @Autowired
    private VisitMapper visitMapper;
    @Autowired
    private ScoreMapper scoreMapper;
    @Autowired
    private ScoreDetailMapper scoreDetailMapper;

    @Override
    public DashboardVO stats() {
        Long sponsorId = null;
        if ("SPONSOR".equals(UserContext.getRole())) {
            sponsorId = UserContext.getUserId();
        }

        QueryWrapper<Student> studentWrapper = new QueryWrapper<>();
        QueryWrapper<Fund> fundWrapper = new QueryWrapper<>();
        QueryWrapper<Visit> visitWrapper = new QueryWrapper<>();
        QueryWrapper<Score> scoreWrapper = new QueryWrapper<>();
        if (sponsorId != null) {
            studentWrapper.eq("sponsor_id", sponsorId);
            fundWrapper.eq("sponsor_id", sponsorId);
            visitWrapper.eq("sponsor_id", sponsorId);
            scoreWrapper.eq("sponsor_id", sponsorId);
        }

        Long studentCount = studentMapper.selectCount(studentWrapper);
        Long fundCount = fundMapper.selectCount(fundWrapper);
        Long visitCount = visitMapper.selectCount(visitWrapper);
        Long scoreCount = scoreMapper.selectCount(scoreWrapper);
        BigDecimal fundTotalAmount = fundMapper.selectTotalAmount(sponsorId);

        DashboardVO vo = new DashboardVO();
        vo.setStudentCount(studentCount);
        vo.setFundCount(fundCount);
        vo.setVisitCount(visitCount);
        vo.setScoreCount(scoreCount);
        vo.setFundTotalAmount(fundTotalAmount != null ? fundTotalAmount : BigDecimal.ZERO);

        DashboardVO.ModuleBar moduleBar = new DashboardVO.ModuleBar();
        moduleBar.setStudent(studentCount);
        moduleBar.setFund(fundCount);
        moduleBar.setVisit(visitCount);
        moduleBar.setScore(scoreCount);
        vo.setModuleBar(moduleBar);

        vo.setMonthlyFunds(fundMapper.selectMonthlyFunds(sponsorId));
        vo.setPurposeDistribution(fundMapper.selectPurposeDistribution(sponsorId));
        vo.setScoreAverages(scoreDetailMapper.selectScoreAverages(sponsorId));
        return vo;
    }
}
