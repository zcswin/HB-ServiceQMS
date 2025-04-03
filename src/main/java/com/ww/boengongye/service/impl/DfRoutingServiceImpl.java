package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ww.boengongye.entity.DfRouting;
import com.ww.boengongye.mapper.DfRoutingMapper;
import com.ww.boengongye.service.DfRoutingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhao
 * @since 2022-09-29
 */
@Service
public class DfRoutingServiceImpl extends ServiceImpl<DfRoutingMapper, DfRouting> implements DfRoutingService {

    @Autowired
    private DfRoutingMapper dfRoutingMapper;

    @Override
    public List<DfRouting> listJoinProcess(Wrapper<DfRouting> wrapper) {
        return dfRoutingMapper.listJoinProcess(wrapper);
    }
}
