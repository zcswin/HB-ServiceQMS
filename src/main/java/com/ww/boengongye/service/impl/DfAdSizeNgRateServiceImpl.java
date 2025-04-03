package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ww.boengongye.entity.DfAdMacOkRate;
import com.ww.boengongye.entity.DfAdSizeNgRate;
import com.ww.boengongye.mapper.DfAdMacOkRateMapper;
import com.ww.boengongye.mapper.DfAdSizeNgRateMapper;
import com.ww.boengongye.service.DfAdSizeNgRateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 员工调机排名--尺寸NG率 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-03-28
 */
@Service
public class DfAdSizeNgRateServiceImpl extends ServiceImpl<DfAdSizeNgRateMapper, DfAdSizeNgRate> implements DfAdSizeNgRateService {

    @Autowired
    private DfAdSizeNgRateMapper dfAdSizeNgRateMapper;

    @Override
    public List<DfAdSizeNgRate> listNgRateByItemInfos(Wrapper<DfAdSizeNgRate> wrapper) {
        return dfAdSizeNgRateMapper.listNgRateByItemInfos(wrapper);
    }

    @Override
    public List<DfAdSizeNgRate> listNgTop10(Wrapper<DfAdSizeNgRate> wrapper) {
        return dfAdSizeNgRateMapper.listNgTop10(wrapper);
    }

    @Override
    public List<DfAdSizeNgRate> listAppearNgInfos(String startTime, String endTime) {
        return dfAdSizeNgRateMapper.listAppearNgInfos(startTime, endTime);
    }

}
