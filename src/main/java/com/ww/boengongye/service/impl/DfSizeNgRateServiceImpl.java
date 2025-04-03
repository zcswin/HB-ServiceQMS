package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfSizeNgRate;
import com.ww.boengongye.mapper.DfSizeNgRateMapper;
import com.ww.boengongye.service.DfSizeNgRateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 尺寸NG率 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-03-05
 */
@Service
public class DfSizeNgRateServiceImpl extends ServiceImpl<DfSizeNgRateMapper, DfSizeNgRate> implements DfSizeNgRateService {

    @Autowired
    private DfSizeNgRateMapper dfSizeNgRateMapper;

    public DfSizeNgRate getAvg(String factory, String process, String project, String linebody, String dayOrNight,
                               String startDate, String endDate) {
        QueryWrapper<DfSizeNgRate> qw = new QueryWrapper<>();
        qw.eq(null != factory, "factory", factory)
                .eq(null != process, "process", process)
                .eq(null != project, "project", project)
                .eq(null != linebody, "linebody", linebody)
                .eq(null != dayOrNight, "day_or_night", dayOrNight)
                .between(null != startDate && null != endDate, "create_time", startDate, endDate);
        return dfSizeNgRateMapper.getAvg(qw);
    }
}
