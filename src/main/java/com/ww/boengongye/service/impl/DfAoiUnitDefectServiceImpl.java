package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ww.boengongye.entity.DfAoiUnitDefect;
import com.ww.boengongye.mapper.DfAoiUnitDefectMapper;
import com.ww.boengongye.service.DfAoiUnitDefectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 单位缺陷机会数 服务实现类
 * </p>
 *
 * @author guangyao
 * @since 2023-08-26
 */
@Service
public class DfAoiUnitDefectServiceImpl extends ServiceImpl<DfAoiUnitDefectMapper, DfAoiUnitDefect> implements DfAoiUnitDefectService {

    @Autowired
    private DfAoiUnitDefectMapper dfAoiUnitDefectMapper;


    @Override
    public Integer getUnitNumberByDefectAndProjectId(Wrapper<Integer> wrapper) {
        return dfAoiUnitDefectMapper.getUnitNumberByDefectAndProjectId(wrapper);
    }
}
