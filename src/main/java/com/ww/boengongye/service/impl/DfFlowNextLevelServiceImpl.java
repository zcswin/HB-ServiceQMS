package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ww.boengongye.entity.DfFlowNextLevel;
import com.ww.boengongye.mapper.DfFlowNextLevelMapper;
import com.ww.boengongye.service.DfFlowNextLevelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 流程的下一级配置 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2022-10-27
 */
@Service
public class DfFlowNextLevelServiceImpl extends ServiceImpl<DfFlowNextLevelMapper, DfFlowNextLevel> implements DfFlowNextLevelService {

    @Autowired
    DfFlowNextLevelMapper DfFlowNextLevelMapper;
    @Override
    public List<DfFlowNextLevel> listNextLevel(Wrapper<DfFlowNextLevel> wrapper) {
        return DfFlowNextLevelMapper.listNextLevel(wrapper);
    }
}
