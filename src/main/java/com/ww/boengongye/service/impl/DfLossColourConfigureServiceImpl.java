package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ww.boengongye.entity.DfLossColourConfigure;
import com.ww.boengongye.mapper.DfLossColourConfigureMapper;
import com.ww.boengongye.service.DfLossColourConfigureService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 漏检率颜色配置 服务实现类
 * </p>
 *
 * @author guangyao
 * @since 2023-08-08
 */
@Service
public class DfLossColourConfigureServiceImpl extends ServiceImpl<DfLossColourConfigureMapper, DfLossColourConfigure> implements DfLossColourConfigureService {

    @Autowired
    DfLossColourConfigureMapper dfLossColourConfigureMapper;

    @Override
    public IPage<DfLossColourConfigure> listJoinPage(IPage<DfLossColourConfigure> page, Wrapper<DfLossColourConfigure> wrapper) {
        return dfLossColourConfigureMapper.listJoinPage(page,wrapper);
    }
}
