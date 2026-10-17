package com.funding.service;

import com.funding.entity.ScoreItem;

import java.util.List;

/**
 * 评分指标服务
 */
public interface ScoreItemService {

    List<ScoreItem> list();

    void save(ScoreItem item);

    void update(ScoreItem item);

    void delete(Long id);
}
