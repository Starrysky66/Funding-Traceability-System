package com.funding.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.funding.entity.ScoreDetail;
import com.funding.vo.DashboardVO;
import com.funding.vo.ScoreDetailVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 评分明细 Mapper
 */
public interface ScoreDetailMapper extends BaseMapper<ScoreDetail> {

    /**
     * 查询某评分单的全部明细（联表 score_item 获取 itemName、weight）
     * 若指标已删除，itemName 显示"已删除指标"，weight 取 0
     */
    @Select("SELECT sd.id, sd.score_id, sd.item_id, " +
            "COALESCE(si.name, '已删除指标') AS item_name, " +
            "COALESCE(si.weight, 0) AS weight, " +
            "sd.item_score " +
            "FROM score_detail sd " +
            "LEFT JOIN score_item si ON sd.item_id = si.id " +
            "WHERE sd.score_id = #{scoreId} ORDER BY sd.id")
    List<ScoreDetailVO> selectByScoreIdWithItem(@Param("scoreId") Long scoreId);

    /**
     * 统计各指标的平均分（按资助人过滤）
     */
    @Select("<script>" +
            "SELECT si.name AS item_name, IFNULL(AVG(sd.item_score),0) AS avg " +
            "FROM score_detail sd " +
            "LEFT JOIN score_item si ON sd.item_id = si.id " +
            "LEFT JOIN score s ON sd.score_id = s.id " +
            "<where><if test='sponsorId != null'>s.sponsor_id = #{sponsorId}</if></where> " +
            "GROUP BY si.id, si.name" +
            "</script>")
    List<DashboardVO.ScoreAvg> selectScoreAverages(@Param("sponsorId") Long sponsorId);
}
