package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ww.boengongye.entity.DfAoiSiCompare;
import com.ww.boengongye.entity.DfAoiSiCompare;
import com.ww.boengongye.mapper.DfAoiSiCompareMapper;
import com.ww.boengongye.service.DfAoiSiCompareService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * AOI SI工厂对比 服务实现类
 * </p>
 *
 * @author guangyao
 * @since 2023-09-07
 */
@Service
public class DfAoiSiCompareServiceImpl extends ServiceImpl<DfAoiSiCompareMapper, DfAoiSiCompare> implements DfAoiSiCompareService {

    @Autowired
    private  DfAoiSiCompareMapper dfAoiSiCompareMapper;

    @Override
    public List<String> getSiFactoryName(Wrapper<String> wrapper) {
        return dfAoiSiCompareMapper.getSiFactoryName(wrapper);
    }

    @Override
    public List<String> getSiSellPlace(Wrapper<String> wrapper) {
        return dfAoiSiCompareMapper.getSiSellPlace(wrapper);
    }

    @Override
    public List<String> getTimeMonth(Wrapper<String> wrapper) {
        return dfAoiSiCompareMapper.getTimeMonth(wrapper);
    }

    @Override
    public List<String> getTimeWeek(Wrapper<String> wrapper) {
        return dfAoiSiCompareMapper.getTimeWeek(wrapper);
    }

    @Override
    public List<String> getTimeDay(Wrapper<String> wrapper) {
        return dfAoiSiCompareMapper.getTimeDay(wrapper);
    }

    @Override
    public List<DfAoiSiCompare> getSiPassPointMonth(Wrapper<DfAoiSiCompare> wrapper, Wrapper<String> wrapper2) {
        return dfAoiSiCompareMapper.getSiPassPointMonth(wrapper,wrapper2);
    }

    @Override
    public List<DfAoiSiCompare> getSiPassPointWeek(Wrapper<DfAoiSiCompare> wrapper, Wrapper<String> wrapper2) {
        return dfAoiSiCompareMapper.getSiPassPointWeek(wrapper,wrapper2);
    }

    @Override
    public List<DfAoiSiCompare> getSiPassPointDay(Wrapper<DfAoiSiCompare> wrapper, Wrapper<String> wrapper2) {
        return dfAoiSiCompareMapper.getSiPassPointDay(wrapper,wrapper2);
    }

    @Override
    public List<DfAoiSiCompare> getSiBatchPassPointMonth(Wrapper<DfAoiSiCompare> wrapper, Wrapper<String> wrapper2) {
        return dfAoiSiCompareMapper.getSiBatchPassPointMonth(wrapper,wrapper2);
    }

    @Override
    public List<DfAoiSiCompare> getSiBatchPassPointWeek(Wrapper<DfAoiSiCompare> wrapper, Wrapper<String> wrapper2) {
        return dfAoiSiCompareMapper.getSiBatchPassPointWeek(wrapper,wrapper2);
    }

    @Override
    public List<DfAoiSiCompare> getSiBatchPassPointDay(Wrapper<DfAoiSiCompare> wrapper, Wrapper<String> wrapper2) {
        return dfAoiSiCompareMapper.getSiBatchPassPointDay(wrapper,wrapper2);
    }

    @Override
    public List<DfAoiSiCompare> getSiDefectPointFactory(Wrapper<DfAoiSiCompare> wrapper) {
        return dfAoiSiCompareMapper.getSiDefectPointFactory(wrapper);
    }

    @Override
    public List<String> getSiDefectNameListFactory(Wrapper<String> wrapper) {
        return dfAoiSiCompareMapper.getSiDefectNameListFactory(wrapper);
    }

    @Override
    public List<DfAoiSiCompare> getSiOneDefectPointListFactory(Wrapper<DfAoiSiCompare> wrapper, Wrapper<DfAoiSiCompare> wrapper2) {
        return dfAoiSiCompareMapper.getSiOneDefectPointListFactory(wrapper,wrapper2);
    }

    @Override
    public List<DfAoiSiCompare> getSiDefectPointSellPlace(Wrapper<DfAoiSiCompare> wrapper) {
        return dfAoiSiCompareMapper.getSiDefectPointSellPlace(wrapper);
    }

    @Override
    public List<String> getSiDefectNameListSellPlace(Wrapper<String> wrapper) {
        return dfAoiSiCompareMapper.getSiDefectNameListSellPlace(wrapper);
    }

    @Override
    public List<DfAoiSiCompare> getSiOneDefectPointListSellPlace(Wrapper<DfAoiSiCompare> wrapper, Wrapper<DfAoiSiCompare> wrapper2) {
        return dfAoiSiCompareMapper.getSiOneDefectPointListSellPlace(wrapper,wrapper2);
    }

    @Override
    public List<String> getSiTypeList(Wrapper<String> wrapper) {
        return dfAoiSiCompareMapper.getSiTypeList(wrapper);
    }

    @Override
    public List<DfAoiSiCompare> getSiDefectPointTop5List(Wrapper<DfAoiSiCompare> wrapper) {
        return dfAoiSiCompareMapper.getSiDefectPointTop5List(wrapper);
    }

    @Override
    public List<DfAoiSiCompare> getSiPassPointAndBatchList(Wrapper<DfAoiSiCompare> wrapper) {
        return dfAoiSiCompareMapper.getSiPassPointAndBatchList(wrapper);
    }

}
