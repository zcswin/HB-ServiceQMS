package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.DailyCheckSummary;

import java.util.List;

public interface DailyCheckSummaryService extends IService<DailyCheckSummary> {

    List<DailyCheckSummary> listDailySummaryPage(QueryWrapper<DailyCheckSummary> ew, int pageOffset, int pageSize);

    int Count(QueryWrapper<DailyCheckSummary> qw);

}
