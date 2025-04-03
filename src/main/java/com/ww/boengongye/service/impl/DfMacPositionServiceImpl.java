package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ww.boengongye.entity.DfControlStandardConfig;
import com.ww.boengongye.entity.DfMacPosition;
import com.ww.boengongye.mapper.DfMacPositionMapper;
import com.ww.boengongye.service.DfMacPositionService;
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
 * @since 2022-08-04
 */
@Service
public class DfMacPositionServiceImpl extends ServiceImpl<DfMacPositionMapper, DfMacPosition> implements DfMacPositionService {


    @Override
    public List<DfMacPosition> listByWorkOrder(Wrapper<DfControlStandardConfig> wrapper) {
        return null;
    }
}
