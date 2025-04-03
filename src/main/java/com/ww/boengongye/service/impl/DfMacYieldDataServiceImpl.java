package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ww.boengongye.entity.DfMacYieldData;
import com.ww.boengongye.mapper.DfMacYieldDataMapper;
import com.ww.boengongye.service.DfMacYieldDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 机台产量数据 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-01-04
 */
@Service
public class DfMacYieldDataServiceImpl extends ServiceImpl<DfMacYieldDataMapper, DfMacYieldData> implements DfMacYieldDataService {
    @Autowired
    private DfMacYieldDataMapper dfMacYieldDataMapper;

    @Override
    public List<DfMacYieldData> listOkRate(Wrapper<DfMacYieldData> wrapper) {
        return dfMacYieldDataMapper.listOkRate(wrapper);
    }

    @Override
    public List<DfMacYieldData> listNgRate(Wrapper<DfMacYieldData> wrapper) {
        return dfMacYieldDataMapper.listNgRate(wrapper);
    }

    @Override
    public List<DfMacYieldData> listOEE(Wrapper<DfMacYieldData> wrapper) {
        return dfMacYieldDataMapper.listOEE(wrapper);
    }

    @Override
    public List<DfMacYieldData> listPassThroughRate(Wrapper<DfMacYieldData> wrapper) {
        return dfMacYieldDataMapper.listPassThroughRate(wrapper);
    }

    @Override
    public List<DfMacYieldData> getMacDetail(Wrapper<DfMacYieldData> wrapper) {
        return dfMacYieldDataMapper.getMacDetail(wrapper);
    }

    @Override
    public List<DfMacYieldData> getProDetail(Wrapper<DfMacYieldData> wrapper) {
        return dfMacYieldDataMapper.getProDetail(wrapper);
    }
}
