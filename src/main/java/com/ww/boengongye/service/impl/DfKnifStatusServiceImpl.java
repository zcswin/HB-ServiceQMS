package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ww.boengongye.entity.DfKnifStatus;
import com.ww.boengongye.entity.Rate3;
import com.ww.boengongye.mapper.DfKnifStatusMapper;
import com.ww.boengongye.service.DfKnifStatusService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 砂轮刀具状态 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-09-21
 */
@Service
public class DfKnifStatusServiceImpl extends ServiceImpl<DfKnifStatusMapper, DfKnifStatus> implements DfKnifStatusService {

    @Autowired
    private DfKnifStatusMapper dfKnifStatusMapper;


    @Override
    public List<Rate3> getKnifeLifeDistribution(Wrapper<DfKnifStatus> wrapper) {
        return dfKnifStatusMapper.getKnifeLifeDistribution(wrapper);
    }
}
