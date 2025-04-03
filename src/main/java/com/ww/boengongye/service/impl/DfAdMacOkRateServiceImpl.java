package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ww.boengongye.entity.DfAdMacOkRate;
import com.ww.boengongye.entity.DfSizeDetail;
import com.ww.boengongye.mapper.DfAdMacOkRateMapper;
import com.ww.boengongye.service.DfAdMacOkRateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 员工调机排名--机台良率 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-03-28
 */
@Service
public class DfAdMacOkRateServiceImpl extends ServiceImpl<DfAdMacOkRateMapper, DfAdMacOkRate> implements DfAdMacOkRateService {

    @Autowired
    private DfAdMacOkRateMapper dfAdMacOkRateMapper;

    @Override
    public List<DfAdMacOkRate> listOkRateBySizeDetail(Wrapper<DfAdMacOkRate> wrapper) {
        return dfAdMacOkRateMapper.listOkRateBySizeDetail(wrapper);
    }

    @Override
    public List<DfAdMacOkRate> listOkRateTop10(Wrapper<DfAdMacOkRate> wrapper) {
        return dfAdMacOkRateMapper.listOkRateTop10(wrapper);
    }

    @Override
    public List<DfAdMacOkRate> listAppearOkRate(Wrapper<DfAdMacOkRate> wrapper) {
        return dfAdMacOkRateMapper.listAppearOkRate(wrapper);
    }
}
