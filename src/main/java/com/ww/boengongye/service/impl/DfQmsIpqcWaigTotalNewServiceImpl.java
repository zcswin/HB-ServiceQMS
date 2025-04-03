package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfQmsIpqcWaigDetailNew;
import com.ww.boengongye.entity.DfQmsIpqcWaigTotal;
import com.ww.boengongye.entity.DfQmsIpqcWaigTotalNew;
import com.ww.boengongye.mapper.DfQmsIpqcWaigTotalNewMapper;
import com.ww.boengongye.service.DfQmsIpqcWaigTotalNewService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author guangyao
 * @since 2023-11-29
 */
@Service
public class DfQmsIpqcWaigTotalNewServiceImpl extends ServiceImpl<DfQmsIpqcWaigTotalNewMapper, DfQmsIpqcWaigTotalNew> implements DfQmsIpqcWaigTotalNewService {

    @Autowired
    private DfQmsIpqcWaigTotalNewMapper dfQmsIpqcWaigTotalNewMapper;

    @Override
    public DfQmsIpqcWaigTotalNew getTotalAndNgCount(Wrapper<DfQmsIpqcWaigTotalNew> wrapper) {
        return dfQmsIpqcWaigTotalNewMapper.getTotalAndNgCount(wrapper);
    }

    @Override
    public List<DfQmsIpqcWaigTotalNew> listWaigExcelData(QueryWrapper<DfQmsIpqcWaigTotalNew> ew) {
        return dfQmsIpqcWaigTotalNewMapper.listWaigExcelData(ew);
    }
}
