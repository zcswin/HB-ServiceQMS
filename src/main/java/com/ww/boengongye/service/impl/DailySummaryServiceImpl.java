package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.entity.DailySummary;
import com.ww.boengongye.mapper.DailySummaryMapper;
import com.ww.boengongye.service.DailySummaryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DailySummaryServiceImpl extends ServiceImpl<DailySummaryMapper, DailySummary> implements DailySummaryService {

    @Resource
    private DailySummaryMapper dailySummaryMapper;


    @Override
    public List<DailySummary> listDailySummaryPage(QueryWrapper<DailySummary> ew, int pageOffset, int pageSize) {
        return dailySummaryMapper.listDailySummaryPage(ew,pageOffset,pageSize);
    }

    @Override
    public int Count(QueryWrapper<DailySummary> qw) {
        return dailySummaryMapper.Count(qw);
    }
}
