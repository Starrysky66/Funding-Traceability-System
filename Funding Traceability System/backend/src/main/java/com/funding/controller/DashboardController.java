package com.funding.controller;

import com.funding.common.Result;
import com.funding.service.DashboardService;
import com.funding.vo.DashboardVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 仪表盘统计接口
 */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/stats")
    public Result<DashboardVO> stats() {
        return Result.success(dashboardService.stats());
    }
}
