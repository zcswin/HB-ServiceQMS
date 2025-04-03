package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ww.boengongye.entity.DfControlStandardConfig;
import com.ww.boengongye.entity.DfMacResOrder;
import com.ww.boengongye.mapper.DfMacResOrderMapper;
import com.ww.boengongye.service.DfMacResOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 机台-工单关系 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-03-12
 */
@Service
public class DfMacResOrderServiceImpl extends ServiceImpl<DfMacResOrderMapper, DfMacResOrder> implements DfMacResOrderService {

    @Autowired
    private DfMacResOrderMapper dfMacResOrderMapper;

    @Override
    public List<DfMacResOrder> listAllJoinWorkOrder(Wrapper<DfMacResOrder> wrapper) {
        return dfMacResOrderMapper.listAllJoinWorkOrder(wrapper);
    }
}
