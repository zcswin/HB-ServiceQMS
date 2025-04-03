package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfFaiPassRate;
import com.ww.boengongye.entity.DfMacRetest;
import com.ww.boengongye.entity.DfSizeNgRate;
import com.ww.boengongye.entity.Rate3;
import com.ww.boengongye.mapper.DfFaiPassRateMapper;
import com.ww.boengongye.mapper.DfMacRetestMapper;
import com.ww.boengongye.mapper.DfMacRevMapper;
import com.ww.boengongye.service.DfMacRetestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

/**
 * <p>
 * 机台复测 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-03-05
 */
@Service
public class DfMacRetestServiceImpl extends ServiceImpl<DfMacRetestMapper, DfMacRetest> implements DfMacRetestService {

    @Autowired
    private DfMacRetestMapper dfMacRetestMapper;

    public List<DfMacRetest> listBastPassRate(String factory, String process, String project, String linebody,
                                                String startDate, String endDate, Integer testType) {

        QueryWrapper<DfMacRetest> qw = new QueryWrapper<>();
        qw.eq(null != factory, "factory", factory)
                .eq(null != process, "process", process)
                .eq(null != project, "project", project)
                .eq(null != linebody, "linebody", linebody)
                .eq(null != testType, "test_type", testType)
                .between(null != startDate && null != endDate, "create_time", startDate, endDate);
        return dfMacRetestMapper.listBestPassRate(qw);
    }

    public List<DfMacRetest> listPoorestMacResponseTime(String factory, String process, String project, String linebody, String dayOrNight,
                                              String startDate, String endDate, Integer testType) {

        QueryWrapper<DfMacRetest> qw = new QueryWrapper<>();
        qw.eq(null != factory, "factory", factory)
                .eq(null != process, "process", process)
                .eq(null != project, "project", project)
                .eq(null != linebody, "linebody", linebody)
                .eq(null != dayOrNight, "day_or_night", dayOrNight)
                .eq(null != testType, "test_type", testType)
                .between(null != startDate && null != endDate, "create_time", startDate, endDate);
        return dfMacRetestMapper.listPoorestMacResponseTime(qw);
    }

    @Override
    public List<Rate3> listDayNightMacOkRate(Wrapper<DfMacRetest> wrapper) {
        return dfMacRetestMapper.listDayNightMacOkRate(wrapper);
    }

    @Override
    public List<Rate3> listDayNightMacOkRateAndResTimeDetail(Wrapper<DfMacRetest> wrapper) {
        return dfMacRetestMapper.listDayNightMacOkRateAndResTimeDetail(wrapper);
    }

    @Override
    public List<Rate3> listMacOkRateTop(Wrapper<DfMacRetest> wrapper) {
        return dfMacRetestMapper.listMacOkRateTop(wrapper);
    }

    @Override
    public List<Rate3> listMacResponseTimeTop(Wrapper<DfMacRetest> wrapper) {
        return dfMacRetestMapper.listMacResponseTimeTop(wrapper);
    }
}
