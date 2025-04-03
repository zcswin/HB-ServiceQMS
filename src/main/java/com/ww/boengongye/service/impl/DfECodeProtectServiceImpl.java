package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ww.boengongye.entity.DfECodeProtect;
import com.ww.boengongye.mapper.DfECodeProtectMapper;
import com.ww.boengongye.service.DfECodeProtectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * E-Code维护 服务实现类
 * </p>
 *
 * @author guangyao
 * @since 2023-08-07
 */
@Service
public class DfECodeProtectServiceImpl extends ServiceImpl<DfECodeProtectMapper, DfECodeProtect> implements DfECodeProtectService {

    @Autowired
    DfECodeProtectMapper dfECodeProtectMapper;

    @Override
    public IPage<DfECodeProtect> listJoinPage(IPage<DfECodeProtect> page, Wrapper<DfECodeProtect> wrapper) {
        return dfECodeProtectMapper.listJoinPage(page,wrapper);
    }
}
