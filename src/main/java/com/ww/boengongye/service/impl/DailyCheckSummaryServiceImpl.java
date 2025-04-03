package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.entity.DailyCheckSummary;
import com.ww.boengongye.mapper.DailyCheckSummaryMapper;
import com.ww.boengongye.service.DailyCheckSummaryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DailyCheckSummaryServiceImpl extends ServiceImpl<DailyCheckSummaryMapper, DailyCheckSummary> implements DailyCheckSummaryService {

    @Resource
    private DailyCheckSummaryMapper dailyCheckSummaryMapper;


    @Override
    public List<DailyCheckSummary> listDailySummaryPage(QueryWrapper<DailyCheckSummary> ew, int pageOffset, int pageSize) {
        return dailyCheckSummaryMapper.listDailyCheckSummaryPage(ew,pageOffset,pageSize);
    }

    @Override
    public int Count(QueryWrapper<DailyCheckSummary> qw) {
        return dailyCheckSummaryMapper.Count(qw);
    }
}
