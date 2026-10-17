package com.funding.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.funding.common.AdminOnly;
import com.funding.common.Result;
import com.funding.entity.Visit;
import com.funding.service.VisitService;
import com.funding.vo.VisitVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * 回访接口
 */
@RestController
@RequestMapping("/api/visits")
public class VisitController {

    @Autowired
    private VisitService visitService;

    @GetMapping
    public Result<IPage<VisitVO>> page(@RequestParam(defaultValue = "1") Integer page,
                                       @RequestParam(defaultValue = "10") Integer size,
                                       @RequestParam(required = false) Long studentId,
                                       @RequestParam(required = false) String keyword) {
        return Result.success(visitService.page(page, size, studentId, keyword));
    }

    @GetMapping("/export")
    @AdminOnly
    public void export(HttpServletResponse response) throws Exception {
        visitService.export(response);
    }

    @GetMapping("/{id}")
    public Result<VisitVO> getById(@PathVariable Long id) {
        return Result.success(visitService.getById(id));
    }

    @PostMapping
    @AdminOnly
    public Result<Void> save(@RequestBody Visit visit) {
        visitService.saveVisit(visit);
        return Result.success();
    }

    @PutMapping("/{id}")
    @AdminOnly
    public Result<Void> update(@PathVariable Long id, @RequestBody Visit visit) {
        visit.setId(id);
        visitService.updateVisit(visit);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @AdminOnly
    public Result<Void> delete(@PathVariable Long id) {
        visitService.deleteVisit(id);
        return Result.success();
    }
}
