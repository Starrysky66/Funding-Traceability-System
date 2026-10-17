package com.funding.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.funding.entity.Fund;
import com.funding.vo.DashboardVO;
import com.funding.vo.FundVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * 资金流水 Mapper
 */
public interface FundMapper extends BaseMapper<Fund> {

    @Select("<script>" +
            "SELECT f.*, s.name AS student_name, u.real_name AS sponsor_name FROM fund f " +
            "LEFT JOIN student s ON f.student_id = s.id " +
            "LEFT JOIN sys_user u ON f.sponsor_id = u.id " +
            "<where>" +
            "<if test='keyword != null and keyword != \"\"'>" +
            "AND (s.name LIKE CONCAT('%',#{keyword},'%') OR f.purpose LIKE CONCAT('%',#{keyword},'%') OR f.remark LIKE CONCAT('%',#{keyword},'%'))" +
            "</if>" +
            "<if test='studentId != null'>" +
            "AND f.student_id = #{studentId}" +
            "</if>" +
            "<if test='sponsorId != null'>" +
            "AND f.sponsor_id = #{sponsorId}" +
            "</if>" +
            "</where>" +
            "ORDER BY f.id DESC" +
            "</script>")
    IPage<FundVO> selectPageWithNames(Page<FundVO> page,
                                      @Param("keyword") String keyword,
                                      @Param("studentId") Long studentId,
                                      @Param("sponsorId") Long sponsorId);

    @Select("SELECT f.*, s.name AS student_name, u.real_name AS sponsor_name FROM fund f " +
            "LEFT JOIN student s ON f.student_id = s.id " +
            "LEFT JOIN sys_user u ON f.sponsor_id = u.id WHERE f.id = #{id}")
    FundVO selectByIdWithNames(@Param("id") Long id);

    @Select("SELECT f.*, s.name AS student_name, u.real_name AS sponsor_name FROM fund f " +
            "LEFT JOIN student s ON f.student_id = s.id " +
            "LEFT JOIN sys_user u ON f.sponsor_id = u.id ORDER BY f.id DESC")
    List<FundVO> selectAllWithNames();

    // ===== 统计相关 =====

    @Select("<script>" +
            "SELECT IFNULL(SUM(amount),0) FROM fund " +
            "<where><if test='sponsorId != null'>sponsor_id = #{sponsorId}</if></where>" +
            "</script>")
    BigDecimal selectTotalAmount(@Param("sponsorId") Long sponsorId);

    @Select("<script>" +
            "SELECT DATE_FORMAT(fund_date, '%Y-%m') AS month, IFNULL(SUM(amount),0) AS amount FROM fund " +
            "<where><if test='sponsorId != null'>sponsor_id = #{sponsorId}</if></where> " +
            "GROUP BY month ORDER BY month" +
            "</script>")
    List<DashboardVO.MonthlyFund> selectMonthlyFunds(@Param("sponsorId") Long sponsorId);

    @Select("<script>" +
            "SELECT purpose AS name, COUNT(*) AS value FROM fund " +
            "<where><if test='sponsorId != null'>sponsor_id = #{sponsorId}</if></where> " +
            "GROUP BY purpose" +
            "</script>")
    List<DashboardVO.PurposeDist> selectPurposeDistribution(@Param("sponsorId") Long sponsorId);
}
