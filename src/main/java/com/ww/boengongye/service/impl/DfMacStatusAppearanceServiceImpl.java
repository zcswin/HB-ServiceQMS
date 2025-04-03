package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ww.boengongye.entity.DfMacStatusAppearance;
import com.ww.boengongye.entity.DfMacStatusSize;
import com.ww.boengongye.entity.DfQmsIpqcWaigTotal;
import com.ww.boengongye.mapper.DfMacStatusAppearanceMapper;
import com.ww.boengongye.service.DfMacStatusAppearanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-03-13
 */
@Service
public class DfMacStatusAppearanceServiceImpl extends ServiceImpl<DfMacStatusAppearanceMapper, DfMacStatusAppearance> implements DfMacStatusAppearanceService {

    @Autowired
    DfMacStatusAppearanceMapper DfMacStatusAppearanceMapper;


    @Override
    public List<DfMacStatusAppearance> listStatus(Wrapper<DfMacStatusAppearance> wrapper) {
        return DfMacStatusAppearanceMapper.listStatus(wrapper);
    }

    @Override
    public List<DfMacStatusAppearance> countByStatus() {
        return DfMacStatusAppearanceMapper.countByStatus();
    }

    @Override
    public List<DfQmsIpqcWaigTotal> preparationTimeout(Wrapper<DfQmsIpqcWaigTotal> wrapper) {
        return DfMacStatusAppearanceMapper.preparationTimeout(wrapper);
    }
}
