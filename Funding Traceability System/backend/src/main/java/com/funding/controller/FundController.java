package com.funding.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.funding.common.AdminOnly;
import com.funding.common.Result;
import com.funding.entity.Fund;
import com.funding.service.FundService;
import com.funding.vo.FundVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * 资金流水接口
 */
@RestController
@RequestMapping("/api/funds")
public class FundController {

    @Autowired
    private FundService fundService;

    @GetMapping
    public Result<IPage<FundVO>> page(@RequestParam(defaultValue = "1") Integer page,
                                      @RequestParam(defaultValue = "10") Integer size,
                                      @RequestParam(required = false) Long studentId,
                                      @RequestParam(required = false) String keyword) {
        return Result.success(fundService.page(page, size, studentId, keyword));
    }

    @GetMapping("/export")
    @AdminOnly
    public void export(HttpServletResponse response) throws Exception {
        fundService.export(response);
    }

    @GetMapping("/{id}")
    public Result<FundVO> getById(@PathVariable Long id) {
        return Result.success(fundService.getById(id));
    }

    @PostMapping
    @AdminOnly
    public Result<Void> save(@RequestBody Fund fund) {
        fundService.saveFund(fund);
        return Result.success();
    }

    @PutMapping("/{id}")
    @AdminOnly
    public Result<Void> update(@PathVariable Long id, @RequestBody Fund fund) {
        fund.setId(id);
        fundService.updateFund(fund);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @AdminOnly
    public Result<Void> delete(@PathVariable Long id) {
        fundService.deleteFund(id);
        return Result.success();
    }
}
