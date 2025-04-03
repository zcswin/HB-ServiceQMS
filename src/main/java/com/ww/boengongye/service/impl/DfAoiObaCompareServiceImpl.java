package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ww.boengongye.entity.DfAoiObaCompare;
import com.ww.boengongye.mapper.DfAoiObaCompareMapper;
import com.ww.boengongye.service.DfAoiObaCompareService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * OBA工厂比较 服务实现类
 * </p>
 *
 * @author guangyao
 * @since 2023-09-04
 */
@Service
public class DfAoiObaCompareServiceImpl extends ServiceImpl<DfAoiObaCompareMapper, DfAoiObaCompare> implements DfAoiObaCompareService {

    @Autowired
    DfAoiObaCompareMapper dfAoiObaCompareMapper;

    @Override
    public List<String> getObaFactoryName(Wrapper<String> wrapper) {
        return dfAoiObaCompareMapper.getObaFactoryName(wrapper);
    }

    @Override
    public List<String> getTimeMonth(Wrapper<String> wrapper) {
        return dfAoiObaCompareMapper.getTimeMonth(wrapper);
    }

    @Override
    public List<String> getTimeWeek(Wrapper<String> wrapper) {
        return dfAoiObaCompareMapper.getTimeWeek(wrapper);
    }

    @Override
    public List<String> getTimeDay(Wrapper<String> wrapper) {
        return dfAoiObaCompareMapper.getTimeDay(wrapper);
    }

    @Override
    public List<DfAoiObaCompare> getObaPassPointMonth(Wrapper<DfAoiObaCompare> wrapper, Wrapper<String> wrapper2) {
        return dfAoiObaCompareMapper.getObaPassPointMonth(wrapper,wrapper2);
    }

    @Override
    public List<DfAoiObaCompare> getObaPassPointWeek(Wrapper<DfAoiObaCompare> wrapper,Wrapper<String> wrapper2) {
        return dfAoiObaCompareMapper.getObaPassPointWeek(wrapper,wrapper2);
    }

    @Override
    public List<DfAoiObaCompare> getObaPassPointDay(Wrapper<DfAoiObaCompare> wrapper, Wrapper<String> wrapper2) {
        return dfAoiObaCompareMapper.getObaPassPointDay(wrapper,wrapper2);
    }

    @Override
    public List<DfAoiObaCompare> getObaBatchPassPointMonth(Wrapper<DfAoiObaCompare> wrapper, Wrapper<String> wrapper2) {
        return dfAoiObaCompareMapper.getObaBatchPassPointMonth(wrapper,wrapper2);
    }

    @Override
    public List<DfAoiObaCompare> getObaBatchPassPointWeek(Wrapper<DfAoiObaCompare> wrapper, Wrapper<String> wrapper2) {
        return dfAoiObaCompareMapper.getObaBatchPassPointWeek(wrapper,wrapper2);
    }

    @Override
    public List<DfAoiObaCompare> getObaBatchPassPointDay(Wrapper<DfAoiObaCompare> wrapper, Wrapper<String> wrapper2) {
        return dfAoiObaCompareMapper.getObaBatchPassPointDay(wrapper,wrapper2);
    }

    @Override
    public List<DfAoiObaCompare> getObaDefectPoint(Wrapper<DfAoiObaCompare> wrapper) {
        return dfAoiObaCompareMapper.getObaDefectPoint(wrapper);
    }

    @Override
    public List<String> getObaDefectNameList(Wrapper<String> wrapper) {
        return dfAoiObaCompareMapper.getObaDefectNameList(wrapper);
    }

    @Override
    public List<DfAoiObaCompare> getObaOneDefectPointList(Wrapper<DfAoiObaCompare> wrapper, Wrapper<DfAoiObaCompare> wrapper2) {
        return dfAoiObaCompareMapper.getObaOneDefectPointList(wrapper,wrapper2);
    }

    @Override
    public List<String> getObaTypeList(Wrapper<String> wrapper) {
        return dfAoiObaCompareMapper.getObaTypeList(wrapper);
    }

    @Override
    public List<DfAoiObaCompare> getObaDefectPointTop5List(Wrapper<DfAoiObaCompare> wrapper) {
        return dfAoiObaCompareMapper.getObaDefectPointTop5List(wrapper);
    }

    @Override
    public List<DfAoiObaCompare> getObaPassPointAndBatchList(Wrapper<DfAoiObaCompare> wrapper) {
        return dfAoiObaCompareMapper.getObaPassPointAndBatchList(wrapper);
    }
}
