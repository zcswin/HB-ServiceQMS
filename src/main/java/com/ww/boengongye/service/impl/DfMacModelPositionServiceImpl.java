package com.ww.boengongye.service.impl;

import com.ww.boengongye.entity.DfMacModelPosition;
import com.ww.boengongye.mapper.DfMacModelPositionMapper;
import com.ww.boengongye.service.DfMacModelPositionService;
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
 * @since 2022-08-08
 */
@Service
public class DfMacModelPositionServiceImpl extends ServiceImpl<DfMacModelPositionMapper, DfMacModelPosition> implements DfMacModelPositionService {

    @Autowired
    DfMacModelPositionMapper DfMacModelPositionMapper;
    @Override
    public List<DfMacModelPosition> listJoinAppearance() {
        return DfMacModelPositionMapper.listJoinAppearance();
    }
}
