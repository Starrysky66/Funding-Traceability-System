package com.funding.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.funding.entity.Student;
import com.funding.vo.OptionVO;
import com.funding.vo.StudentVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 学生服务
 */
public interface StudentService {

    IPage<StudentVO> page(Integer page, Integer size, String keyword);

    StudentVO getById(Long id);

    void saveStudent(Student student);

    void updateStudent(Student student);

    void deleteStudent(Long id);

    List<OptionVO> options();

    void export(HttpServletResponse response) throws Exception;
}
