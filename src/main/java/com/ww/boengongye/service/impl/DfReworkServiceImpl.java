package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ww.boengongye.entity.DfRework;
import com.ww.boengongye.mapper.DfReworkMapper;
import com.ww.boengongye.service.DfReworkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 返工表 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2022-12-19
 */
@Service
public class DfReworkServiceImpl extends ServiceImpl<DfReworkMapper, DfRework> implements DfReworkService {


    @Autowired
    DfReworkMapper DfReworkMapper;

    @Override
    public IPage<DfRework> listJoinIds(IPage<DfRework> page, Wrapper<DfRework> wrapper) {
        return DfReworkMapper.listJoinIds(page,wrapper);
    }
}
