package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ww.boengongye.entity.DfProcessProjectConfig;
import com.ww.boengongye.mapper.DfProcessProjectConfigMapper;
import com.ww.boengongye.service.DfProcessProjectConfigService;
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
 * @since 2023-04-26
 */
@Service
public class DfProcessProjectConfigServiceImpl extends ServiceImpl<DfProcessProjectConfigMapper, DfProcessProjectConfig> implements DfProcessProjectConfigService {

    @Autowired
    DfProcessProjectConfigMapper DfProcessProjectConfigMapper;

    @Override
    public List<DfProcessProjectConfig> listDistinct(Wrapper<DfProcessProjectConfig> wrapper) {
        return DfProcessProjectConfigMapper.listDistinct(wrapper);
    }
}
