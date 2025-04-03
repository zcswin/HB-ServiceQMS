package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfGroupAdjustment;
import com.ww.boengongye.entity.DfSizeNgRate;
import com.ww.boengongye.mapper.DfGroupAdjustmentMapper;
import com.ww.boengongye.service.DfGroupAdjustmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

/**
 * <p>
 * 小组调机能力 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-03-04
 */
@Service
public class DfGroupAdjustmentServiceImpl extends ServiceImpl<DfGroupAdjustmentMapper, DfGroupAdjustment> implements DfGroupAdjustmentService {

    @Autowired
    private DfGroupAdjustmentMapper dfGroupAdjustmentMapper;

    public List<DfGroupAdjustment> listBestRate(String factory, String process, String project, String linebody, String dayOrNight,
                                                String startDate, String endDate, Integer testType) {

        QueryWrapper<DfSizeNgRate> qw = new QueryWrapper<>();
        qw.eq(!"".equals(factory) && null != factory, "gp.factory", factory)
                .eq(!"".equals(process) && null != process, "gp.process", process)
                .eq(!"".equals(project) && null != project, "aj.project", project)
                .eq(!"".equals(linebody) && null != linebody, "aj.linebody", linebody)
                .eq(!"".equals(dayOrNight) && null != dayOrNight, "aj.day_or_night", dayOrNight)
                .eq(null != testType, "aj.test_type", testType)
                .between(null != startDate && null != endDate, "aj.create_time", startDate, endDate);
        return dfGroupAdjustmentMapper.listBestRate(qw);
    }
}
