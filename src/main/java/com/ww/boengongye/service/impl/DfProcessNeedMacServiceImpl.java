package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ww.boengongye.entity.DfProcessNeedMac;
import com.ww.boengongye.mapper.DfProcessNeedMacMapper;
import com.ww.boengongye.service.DfProcessNeedMacService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 工序需求机台数量表 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2022-12-20
 */
@Service
public class DfProcessNeedMacServiceImpl extends ServiceImpl<DfProcessNeedMacMapper, DfProcessNeedMac> implements DfProcessNeedMacService {

    @Autowired
    DfProcessNeedMacMapper DfProcessNeedMacMapper;

    @Override
    public IPage<DfProcessNeedMac> listJoinIds(IPage<DfProcessNeedMac> page, Wrapper<DfProcessNeedMac> wrapper) {
        return DfProcessNeedMacMapper.listJoinIds(page, wrapper);
    }
}
