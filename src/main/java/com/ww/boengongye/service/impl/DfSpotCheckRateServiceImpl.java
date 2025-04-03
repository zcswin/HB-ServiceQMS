package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ww.boengongye.entity.DfControlStandardStatus;
import com.ww.boengongye.entity.DfSpotCheckRate;
import com.ww.boengongye.mapper.DfSpotCheckRateMapper;
import com.ww.boengongye.service.DfSpotCheckRateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 点检率表 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2022-10-14
 */
@Service
public class DfSpotCheckRateServiceImpl extends ServiceImpl<DfSpotCheckRateMapper, DfSpotCheckRate> implements DfSpotCheckRateService {

    @Autowired
    private DfSpotCheckRateMapper dfSpotCheckRateMapper;

    @Override
    public IPage<DfSpotCheckRate> pageJoinFactory(IPage<DfSpotCheckRate> page, Wrapper<DfSpotCheckRate> wrapper) {
        return dfSpotCheckRateMapper.pageJoinFactory(page, wrapper);
    }
}
