package com.funding.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.funding.entity.Visit;
import com.funding.vo.VisitVO;

import javax.servlet.http.HttpServletResponse;

/**
 * 回访服务
 */
public interface VisitService {

    IPage<VisitVO> page(Integer page, Integer size, Long studentId, String keyword);

    VisitVO getById(Long id);

    void saveVisit(Visit visit);

    void updateVisit(Visit visit);

    void deleteVisit(Long id);

    void export(HttpServletResponse response) throws Exception;
}
