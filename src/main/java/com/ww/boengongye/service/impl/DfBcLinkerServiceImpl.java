package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ww.boengongye.entity.DfBcLinker;
import com.ww.boengongye.mapper.DfBcLinkerMapper;
import com.ww.boengongye.service.DfBcLinkerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * BC-linker表 服务实现类
 * </p>
 *
 * @author guangyao
 * @since 2023-09-09
 */
@Service
public class DfBcLinkerServiceImpl extends ServiceImpl<DfBcLinkerMapper, DfBcLinker> implements DfBcLinkerService {

    @Autowired
    private DfBcLinkerMapper dfBcLinkerMapper;


    @Override
    public List<String> getTimeList(Wrapper<String> wrapper) {
        return dfBcLinkerMapper.getTimeList(wrapper);
    }

    @Override
    public List<DfBcLinker> getDayDataList(Wrapper<DfBcLinker> wrapper) {
        return dfBcLinkerMapper.getDayDataList(wrapper);
    }

    @Override
    public List<DfBcLinker> getNightDataList(Wrapper<DfBcLinker> wrapper) {
        return dfBcLinkerMapper.getNightDataList(wrapper);
    }

    @Override
    public List<DfBcLinker> getCipherAndTraceCardPointList(Wrapper<DfBcLinker> wrapper) {
        return dfBcLinkerMapper.getCipherAndTraceCardPointList(wrapper);
    }

    @Override
    public List<DfBcLinker> getDefectPointTop3List(Wrapper<DfBcLinker> wrapper) {
        return dfBcLinkerMapper.getDefectPointTop3List(wrapper);
    }
}
