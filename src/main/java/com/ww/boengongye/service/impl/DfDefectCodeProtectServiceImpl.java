package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ww.boengongye.entity.DfDefectCodeProtect;
import com.ww.boengongye.mapper.DfDefectCodeProtectMapper;
import com.ww.boengongye.service.DfDefectCodeProtectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 不良代码维护 服务实现类
 * </p>
 *
 * @author guangyao
 * @since 2023-08-07
 */
@Service
public class DfDefectCodeProtectServiceImpl extends ServiceImpl<DfDefectCodeProtectMapper, DfDefectCodeProtect> implements DfDefectCodeProtectService {

    @Autowired
    DfDefectCodeProtectMapper dfDefectCodeProtectMapper;

    @Override
    public IPage<DfDefectCodeProtect> listJoinPage(IPage<DfDefectCodeProtect> page, Wrapper<DfDefectCodeProtect> wrapper) {
        return dfDefectCodeProtectMapper.listJoinPage(page,wrapper);
    }

    @Override
    public List<DfDefectCodeProtect> getAllList() {
        return dfDefectCodeProtectMapper.getAllList();
    }

}
