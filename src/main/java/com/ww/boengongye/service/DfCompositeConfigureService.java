package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfCompositeConfigure;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.mapper.DfCompositeConfigureMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 综合配置 服务类
 * </p>
 *
 * @author guangyao
 * @since 2023-08-08
 */
public interface DfCompositeConfigureService extends IService<DfCompositeConfigure> {
    IPage<DfCompositeConfigure> listJoinPage(IPage<DfCompositeConfigure> page, @Param(Constants.WRAPPER) Wrapper<DfCompositeConfigure> wrapper);
}
