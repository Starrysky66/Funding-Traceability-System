package com.funding.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.funding.common.BusinessException;
import com.funding.common.UserContext;
import com.funding.entity.Score;
import com.funding.entity.ScoreDetail;
import com.funding.entity.ScoreItem;
import com.funding.mapper.ScoreDetailMapper;
import com.funding.mapper.ScoreItemMapper;
import com.funding.mapper.ScoreMapper;
import com.funding.service.ScoreService;
import com.funding.util.ExcelUtils;
import com.funding.vo.ScoreDetailVO;
import com.funding.vo.ScoreVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 评分服务实现
 * 保存评分时按 score_item.weight 加权自动计算 total_score（忽略请求中的 totalScore）
 */
@Service
public class ScoreServiceImpl implements ScoreService {

    @Autowired
    private ScoreMapper scoreMapper;
    @Autowired
    private ScoreDetailMapper scoreDetailMapper;
    @Autowired
    private ScoreItemMapper scoreItemMapper;

    @Override
    public IPage<ScoreVO> page(Integer page, Integer size, Long studentId) {
        Page<ScoreVO> p = new Page<>(page, size);
        Long sponsorId = null;
        if ("SPONSOR".equals(UserContext.getRole())) {
            sponsorId = UserContext.getUserId();
        }
        return scoreMapper.selectPageWithNames(p, studentId, sponsorId);
    }

    @Override
    public ScoreVO getById(Long id) {
        ScoreVO score = scoreMapper.selectByIdWithNames(id);
        if (score == null) {
            return null;
        }
        if ("SPONSOR".equals(UserContext.getRole())
                && (score.getSponsorId() == null || !score.getSponsorId().equals(UserContext.getUserId()))) {
            throw new BusinessException(403, "权限不足");
        }
        // 查询明细（联表 score_item）
        List<ScoreDetailVO> details = scoreDetailMapper.selectByScoreIdWithItem(id);
        score.setDetails(details);
        return score;
    }

    /**
     * 计算加权总分：total = Σ(item_score × weight/100)，保留2位小数（HALF_UP）
     * 使用当前全部 score_item，若某项无对应明细则按0分处理
     */
    private BigDecimal calcTotal(List<ScoreDetailVO> details) {
        List<ScoreItem> items = scoreItemMapper.selectList(null);
        Map<Long, BigDecimal> weightMap = items.stream()
                .collect(Collectors.toMap(ScoreItem::getId, ScoreItem::getWeight));
        BigDecimal hundred = new BigDecimal("100");
        BigDecimal total = BigDecimal.ZERO;
        if (details != null) {
            for (ScoreDetailVO d : details) {
                if (d.getItemId() == null || d.getItemScore() == null) {
                    continue;
                }
                BigDecimal weight = weightMap.getOrDefault(d.getItemId(), BigDecimal.ZERO);
                // item_score × weight / 100
                BigDecimal contribution = d.getItemScore()
                        .multiply(weight)
                        .divide(hundred, 2, RoundingMode.HALF_UP);
                total = total.add(contribution);
            }
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveScore(ScoreVO vo) {
        Score score = new Score();
        score.setStudentId(vo.getStudentId());
        score.setSponsorId(vo.getSponsorId());
        score.setScoreDate(vo.getScoreDate());
        score.setRemark(vo.getRemark());
        // 忽略请求中的 totalScore，后端自动计算
        score.setTotalScore(calcTotal(vo.getDetails()));
        scoreMapper.insert(score);
        // 插入明细
        if (vo.getDetails() != null) {
            for (ScoreDetailVO d : vo.getDetails()) {
                ScoreDetail detail = new ScoreDetail();
                detail.setScoreId(score.getId());
                detail.setItemId(d.getItemId());
                detail.setItemScore(d.getItemScore());
                scoreDetailMapper.insert(detail);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateScore(ScoreVO vo) {
        Score score = new Score();
        score.setId(vo.getId());
        score.setStudentId(vo.getStudentId());
        score.setSponsorId(vo.getSponsorId());
        score.setScoreDate(vo.getScoreDate());
        score.setRemark(vo.getRemark());
        score.setTotalScore(calcTotal(vo.getDetails()));
        scoreMapper.updateById(score);
        // 删除旧明细，重新插入
        scoreDetailMapper.delete(new QueryWrapper<ScoreDetail>().eq("score_id", vo.getId()));
        if (vo.getDetails() != null) {
            for (ScoreDetailVO d : vo.getDetails()) {
                ScoreDetail detail = new ScoreDetail();
                detail.setScoreId(vo.getId());
                detail.setItemId(d.getItemId());
                detail.setItemScore(d.getItemScore());
                scoreDetailMapper.insert(detail);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteScore(Long id) {
        scoreMapper.deleteById(id);
        scoreDetailMapper.delete(new QueryWrapper<ScoreDetail>().eq("score_id", id));
    }

    @Override
    public void export(HttpServletResponse response) throws Exception {
        List<ScoreVO> list = scoreMapper.selectAllWithNames();
        List<String> headers = Arrays.asList(
                "ID", "学生", "资助人", "评分日期", "总分", "各指标分", "备注", "创建时间");
        List<List<Object>> rows = new ArrayList<>();
        for (ScoreVO s : list) {
            // 查询明细拼装"学习情况:90,生活改善:85,..."到一列
            List<ScoreDetailVO> details = scoreDetailMapper.selectByScoreIdWithItem(s.getId());
            List<Object> row = new ArrayList<>();
            row.add(s.getId());
            row.add(s.getStudentName());
            row.add(s.getSponsorName());
            row.add(s.getScoreDate() != null ? s.getScoreDate().toString() : "");
            row.add(s.getTotalScore());
            StringBuilder sb = new StringBuilder();
            for (ScoreDetailVO d : details) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(d.getItemName()).append(":").append(d.getItemScore());
            }
            row.add(sb.toString());
            row.add(s.getRemark() != null ? s.getRemark() : "");
            row.add(s.getCreateTime() != null ? s.getCreateTime().toString() : "");
            rows.add(row);
        }
        ExcelUtils.writeWorkbook(response, "评分记录.xlsx", headers, rows);
    }
}
