package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.entity.DfFaiPassRate;
import com.ww.boengongye.mapper.DfFaiPassRateMapper;
import com.ww.boengongye.service.DfFaiPassRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 首检通过率 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-03-04
 */
@Service
public class DfFaiPassRateServiceImpl extends ServiceImpl<DfFaiPassRateMapper, DfFaiPassRate> implements DfFaiPassRateService {

    @Autowired
    private DfFaiPassRateMapper dfFaiPassRateMapper;

    public DfFaiPassRate getRate(String factory, String process, String project, String linebody, String dayOrNight,
                                 String startDate, String endDate, Integer testType) {

        QueryWrapper<DfFaiPassRate> qw = new QueryWrapper<>();
        qw.eq(null != factory, "factory", factory)
                .eq(null != process, "process", process)
                .eq(null != project, "project", project)
                .eq(null != linebody, "linebody", linebody)
                .eq(null != dayOrNight, "day_or_night", dayOrNight)
                .eq(null != testType, "test_type", testType)
                .between(null != startDate && null != endDate, "create_time", startDate, endDate);
        return dfFaiPassRateMapper.getRate(qw); // 查询
    }
}
