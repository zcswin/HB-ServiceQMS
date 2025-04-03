package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ww.boengongye.entity.DfAoiSeatProtect;
import com.ww.boengongye.mapper.DfAoiSeatProtectMapper;
import com.ww.boengongye.service.DfAoiSeatProtectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 工位维护 服务实现类
 * </p>
 *
 * @author guangyao
 * @since 2023-08-04
 */
@Service
public class DfAoiSeatProtectServiceImpl extends ServiceImpl<DfAoiSeatProtectMapper, DfAoiSeatProtect> implements DfAoiSeatProtectService {

    @Autowired
    DfAoiSeatProtectMapper dfAoiSeatProtectMapper;

    @Override
    public IPage<DfAoiSeatProtect> listJoinPage(IPage<DfAoiSeatProtect> page, Wrapper<DfAoiSeatProtect> wrapper) {
        return dfAoiSeatProtectMapper.listJoinPage(page,wrapper);
    }

    @Override
    public List<DfAoiSeatProtect> getAllList() {
        return dfAoiSeatProtectMapper.getAllList();
    }

    @Override
    public DfAoiSeatProtect getSeatProtectByUserCode(Wrapper<DfAoiSeatProtect> wrapper) {
        return dfAoiSeatProtectMapper.getSeatProtectByUserCode(wrapper);
    }
}
