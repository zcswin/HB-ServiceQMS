package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.entity.DfIqcMaterialConfig;
import com.ww.boengongye.mapper.DfIqcMaterialConfigMapper;
import com.ww.boengongye.service.DfIqcMaterialConfigService;
import org.springframework.stereotype.Service;

import java.util.List;

// 实现类
@Service
public class DfIqcMaterialConfigServiceImpl extends ServiceImpl<DfIqcMaterialConfigMapper, DfIqcMaterialConfig>
        implements DfIqcMaterialConfigService {

    @Override
    public List<DfIqcMaterialConfig> listConfigWithJoin(Wrapper<DfIqcMaterialConfig> wrapper) {
        return baseMapper.listConfigWithJoin(wrapper);
    }
}