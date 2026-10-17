package com.funding.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.funding.entity.ScoreItem;
import com.funding.mapper.ScoreItemMapper;
import com.funding.service.ScoreItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 评分指标服务实现
 */
@Service
public class ScoreItemServiceImpl implements ScoreItemService {

    @Autowired
    private ScoreItemMapper scoreItemMapper;

    @Override
    public List<ScoreItem> list() {
        return scoreItemMapper.selectList(new QueryWrapper<ScoreItem>().orderByAsc("id"));
    }

    @Override
    public void save(ScoreItem item) {
        item.setId(null);
        scoreItemMapper.insert(item);
    }

    @Override
    public void update(ScoreItem item) {
        scoreItemMapper.updateById(item);
    }

    @Override
    public void delete(Long id) {
        scoreItemMapper.deleteById(id);
    }
}
