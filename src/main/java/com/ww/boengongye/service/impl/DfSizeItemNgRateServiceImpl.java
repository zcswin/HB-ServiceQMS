package com.ww.boengongye.service.impl;

import com.ww.boengongye.entity.DfSizeItemNgRate;
import com.ww.boengongye.mapper.DfSizeItemNgRateMapper;
import com.ww.boengongye.service.DfSizeItemNgRateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 尺寸看板汇总表--尺寸NG TOP 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-07-04
 */
@Service
public class DfSizeItemNgRateServiceImpl extends ServiceImpl<DfSizeItemNgRateMapper, DfSizeItemNgRate> implements DfSizeItemNgRateService {

    @Autowired
    private DfSizeItemNgRateMapper dfSizeItemNgRateMapper;
    @Override
    public List<DfSizeItemNgRate> listSizeItemNgRate(String startTime, String endTime) {
        return dfSizeItemNgRateMapper.listSizeItemNgRate(startTime, endTime);
    }

    @Override
    public List<DfSizeItemNgRate> listAllProcessNgItemNgRate() {
        return dfSizeItemNgRateMapper.listAllProcessNgItemNgRate();
    }
}
