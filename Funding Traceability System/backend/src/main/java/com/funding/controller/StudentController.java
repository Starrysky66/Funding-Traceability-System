package com.funding.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.funding.common.AdminOnly;
import com.funding.common.Result;
import com.funding.entity.Student;
import com.funding.service.StudentService;
import com.funding.vo.OptionVO;
import com.funding.vo.StudentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 学生接口
 */
@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public Result<IPage<StudentVO>> page(@RequestParam(defaultValue = "1") Integer page,
                                          @RequestParam(defaultValue = "10") Integer size,
                                          @RequestParam(required = false) String keyword) {
        return Result.success(studentService.page(page, size, keyword));
    }

    @GetMapping("/options")
    public Result<List<OptionVO>> options() {
        return Result.success(studentService.options());
    }

    @GetMapping("/export")
    @AdminOnly
    public void export(HttpServletResponse response) throws Exception {
        studentService.export(response);
    }

    @GetMapping("/{id}")
    public Result<StudentVO> getById(@PathVariable Long id) {
        return Result.success(studentService.getById(id));
    }

    @PostMapping
    @AdminOnly
    public Result<Void> save(@RequestBody Student student) {
        studentService.saveStudent(student);
        return Result.success();
    }

    @PutMapping("/{id}")
    @AdminOnly
    public Result<Void> update(@PathVariable Long id, @RequestBody Student student) {
        student.setId(id);
        studentService.updateStudent(student);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @AdminOnly
    public Result<Void> delete(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return Result.success();
    }
}
