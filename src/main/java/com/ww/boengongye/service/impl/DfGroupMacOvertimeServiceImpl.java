package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfGroupMacOvertime;
import com.ww.boengongye.entity.DfSizeNgRate;
import com.ww.boengongye.entity.Rate3;
import com.ww.boengongye.mapper.DfGroupMacOvertimeMapper;
import com.ww.boengongye.service.DfGroupMacOvertimeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

/**
 * <p>
 * 小组机台超时率 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-03-02
 */
@Service
public class DfGroupMacOvertimeServiceImpl extends ServiceImpl<DfGroupMacOvertimeMapper, DfGroupMacOvertime> implements DfGroupMacOvertimeService {

    @Autowired
    private DfGroupMacOvertimeMapper dfGroupMacOvertimeMapper;

    public List<DfGroupMacOvertime> listOvertimeRate(String factory, String process, String project, String linebody, String dayOrNight,
                                                     String startDate, String endDate, Integer testType) {

        QueryWrapper<DfSizeNgRate> qw = new QueryWrapper<>();
        qw.eq(null != factory && !"".equals(factory), "gp.factory", factory)
                .eq(null != process && !"".equals(process), "gp.process", process)
                .eq(null != project && !"".equals(project), "ot.project", project)
                .eq(null != linebody && !"".equals(linebody), "ot.linebody", linebody)
                .eq(null != dayOrNight && !"".equals(dayOrNight), "ot.day_or_night", dayOrNight)
                .eq(null != testType, "ot.test_type", testType)
                .between(null != startDate && null != endDate && !"".equals(startDate) && !"".equals(endDate),
                        "ot.test_time", startDate, endDate);
        return dfGroupMacOvertimeMapper.listOverTimeRate(qw);
    }

    public List<DfGroupMacOvertime> listOvertimeRate2(String factory, String process, String project, String linebody, String dayOrNight,
                                                     String startDate, String endDate, Integer testType) {

        QueryWrapper<DfSizeNgRate> qw = new QueryWrapper<>();
        qw.eq(null != factory && !"".equals(factory), "gp.factory", factory)
                .eq(null != process && !"".equals(process), "gp.process", process)
                .eq(null != project && !"".equals(project), "ot.project", project)
                .eq(null != linebody && !"".equals(linebody), "ot.linebody", linebody)
                .eq(null != dayOrNight && !"".equals(dayOrNight), "ot.day_or_night", dayOrNight)
                .eq(null != testType, "ot.test_type", testType)
                .between(null != startDate && null != endDate && !"".equals(startDate) && !"".equals(endDate),
                        "ot.test_time", startDate, endDate);
        return dfGroupMacOvertimeMapper.listOverTimeRate2(qw);
    }

    @Override
    public List<Rate3> listOverTimeRateGroupByLineBody(Wrapper<DfGroupMacOvertime> wrapper) {
        return dfGroupMacOvertimeMapper.listOverTimeRateGroupByLineBody(wrapper);
    }

    @Override
    public List<Rate3> listOverTimeRateGroupByRespon(Wrapper<DfGroupMacOvertime> wrapper) {
        return dfGroupMacOvertimeMapper.listOverTimeRateGroupByRespon(wrapper);
    }

    @Override
    public List<Rate3> listOverTimeRateGroupByResponTop(Wrapper<DfGroupMacOvertime> wrapper) {
        return dfGroupMacOvertimeMapper.listOverTimeRateGroupByResponTop(wrapper);
    }
}
