package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ww.boengongye.entity.DfInventory;
import com.ww.boengongye.mapper.DfInventoryMapper;
import com.ww.boengongye.service.DfInventoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 消耗品库存表
 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2022-11-22
 */
@Service
public class DfInventoryServiceImpl extends ServiceImpl<DfInventoryMapper, DfInventory> implements DfInventoryService {

    @Autowired
    DfInventoryMapper DfInventoryMapper;

    @Override
    public IPage<DfInventory> listJoinIds(IPage<DfInventory> page, Wrapper<DfInventory> wrapper) {
        return DfInventoryMapper.listJoinIds(page, wrapper);
    }
}
