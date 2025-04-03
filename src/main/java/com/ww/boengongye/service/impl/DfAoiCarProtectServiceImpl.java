package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ww.boengongye.entity.DfAoiCarProtect;
import com.ww.boengongye.mapper.DfAoiCarProtectMapper;
import com.ww.boengongye.service.DfAoiCarProtectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 车间维护 服务实现类
 * </p>
 *
 * @author guangyao
 * @since 2023-08-03
 */
@Service
public class DfAoiCarProtectServiceImpl extends ServiceImpl<DfAoiCarProtectMapper, DfAoiCarProtect> implements DfAoiCarProtectService {

    @Autowired
    DfAoiCarProtectMapper dfAoiCarProtectMapper;

    @Override
    public IPage<DfAoiCarProtect> listJoinPage(IPage<DfAoiCarProtect> page, Wrapper<DfAoiCarProtect> wrapper) {
        return dfAoiCarProtectMapper.listJoinPage(page,wrapper);
    }
}
