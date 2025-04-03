package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ww.boengongye.entity.DfCompositeConfigure;
import com.ww.boengongye.mapper.DfCompositeConfigureMapper;
import com.ww.boengongye.service.DfCompositeConfigureService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 综合配置 服务实现类
 * </p>
 *
 * @author guangyao
 * @since 2023-08-08
 */
@Service
public class DfCompositeConfigureServiceImpl extends ServiceImpl<DfCompositeConfigureMapper, DfCompositeConfigure> implements DfCompositeConfigureService {

    @Autowired
    DfCompositeConfigureMapper dfCompositeConfigureMapper;

    @Override
    public IPage<DfCompositeConfigure> listJoinPage(IPage<DfCompositeConfigure> page, Wrapper<DfCompositeConfigure> wrapper) {
        return dfCompositeConfigureMapper.listJoinPage(page,wrapper);
    }
}
