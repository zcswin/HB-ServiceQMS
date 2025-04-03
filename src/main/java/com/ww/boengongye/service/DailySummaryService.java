package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.DailySummary;

import java.util.List;

public interface DailySummaryService extends IService<DailySummary> {

    List<DailySummary> listDailySummaryPage(QueryWrapper<DailySummary> ew, int pageOffset, int pageSize);

    int Count(QueryWrapper<DailySummary> qw);

}
