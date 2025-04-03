package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfInventory;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 消耗品库存表
 服务类
 * </p>
 *
 * @author zhao
 * @since 2022-11-22
 */
public interface DfInventoryService extends IService<DfInventory> {
    IPage<DfInventory> listJoinIds(IPage<DfInventory> page, @Param(Constants.WRAPPER) Wrapper<DfInventory> wrapper);
}
