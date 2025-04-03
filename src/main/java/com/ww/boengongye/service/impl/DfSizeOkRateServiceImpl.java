package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ww.boengongye.entity.DfSizeOkRate;
import com.ww.boengongye.mapper.DfSizeOkRateMapper;
import com.ww.boengongye.service.DfSizeOkRateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 尺寸看板汇总表--尺寸良率汇总 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-08-15
 */
@Service
public class DfSizeOkRateServiceImpl extends ServiceImpl<DfSizeOkRateMapper, DfSizeOkRate> implements DfSizeOkRateService {

    @Autowired
    private DfSizeOkRateMapper dfSizeOkRateMapper;

    @Override
    public List<DfSizeOkRate> updateDate() {
        return dfSizeOkRateMapper.updateDate();
    }

    @Override
    public List<DfSizeOkRate> listSizeOkRate(Wrapper<DfSizeOkRate> wrapper) {
        return dfSizeOkRateMapper.listSizeOkRate(wrapper);
    }
}
