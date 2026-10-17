package com.funding.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.funding.entity.Fund;
import com.funding.vo.FundVO;

import javax.servlet.http.HttpServletResponse;

/**
 * 资金流水服务
 */
public interface FundService {

    IPage<FundVO> page(Integer page, Integer size, Long studentId, String keyword);

    FundVO getById(Long id);

    void saveFund(Fund fund);

    void updateFund(Fund fund);

    void deleteFund(Long id);

    void export(HttpServletResponse response) throws Exception;
}
