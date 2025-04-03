package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ww.boengongye.entity.DfProject;
import com.ww.boengongye.mapper.DfProjectMapper;
import com.ww.boengongye.service.DfProjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhao
 * @since 2022-09-16
 */
@Service
public class DfProjectServiceImpl extends ServiceImpl<DfProjectMapper, DfProject> implements DfProjectService {

    @Autowired
    DfProjectMapper DfProjectMapper;

    @Override
    public IPage<DfProject> listJoinIds(IPage<DfProject> page, Wrapper<DfProject> wrapper) {
        return DfProjectMapper.listJoinIds(page, wrapper);
    }
}
