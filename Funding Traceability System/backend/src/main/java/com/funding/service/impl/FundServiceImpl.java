package com.funding.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.funding.common.BusinessException;
import com.funding.common.UserContext;
import com.funding.entity.Fund;
import com.funding.mapper.FundMapper;
import com.funding.service.FundService;
import com.funding.util.ExcelUtils;
import com.funding.vo.FundVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 资金流水服务实现
 * SPONSOR 查询一律按当前登录 userId 过滤 sponsor_id
 */
@Service
public class FundServiceImpl implements FundService {

    @Autowired
    private FundMapper fundMapper;

    @Override
    public IPage<FundVO> page(Integer page, Integer size, Long studentId, String keyword) {
        Page<FundVO> p = new Page<>(page, size);
        Long sponsorId = null;
        if ("SPONSOR".equals(UserContext.getRole())) {
            sponsorId = UserContext.getUserId();
        }
        return fundMapper.selectPageWithNames(p, keyword, studentId, sponsorId);
    }

    @Override
    public FundVO getById(Long id) {
        FundVO fund = fundMapper.selectByIdWithNames(id);
        if (fund == null) {
            return null;
        }
        if ("SPONSOR".equals(UserContext.getRole())
                && (fund.getSponsorId() == null || !fund.getSponsorId().equals(UserContext.getUserId()))) {
            throw new BusinessException(403, "权限不足");
        }
        return fund;
    }

    @Override
    public void saveFund(Fund fund) {
        fund.setId(null);
        fundMapper.insert(fund);
    }

    @Override
    public void updateFund(Fund fund) {
        fundMapper.updateById(fund);
    }

    @Override
    public void deleteFund(Long id) {
        fundMapper.deleteById(id);
    }

    @Override
    public void export(HttpServletResponse response) throws Exception {
        List<FundVO> list = fundMapper.selectAllWithNames();
        List<String> headers = Arrays.asList(
                "ID", "学生", "资助人", "金额", "资助日期", "用途", "备注", "创建时间");
        List<List<Object>> rows = new ArrayList<>();
        for (FundVO f : list) {
            List<Object> row = new ArrayList<>();
            row.add(f.getId());
            row.add(f.getStudentName());
            row.add(f.getSponsorName());
            row.add(f.getAmount());
            row.add(f.getFundDate() != null ? f.getFundDate().toString() : "");
            row.add(f.getPurpose());
            row.add(f.getRemark());
            row.add(f.getCreateTime() != null ? f.getCreateTime().toString() : "");
            rows.add(row);
        }
        ExcelUtils.writeWorkbook(response, "资金流水.xlsx", headers, rows);
    }
}
