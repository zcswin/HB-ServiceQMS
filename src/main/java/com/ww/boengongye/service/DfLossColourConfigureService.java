package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfLossColourConfigure;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;


/**
 * <p>
 * 漏检率颜色配置 服务类
 * </p>
 *
 * @author guangyao
 * @since 2023-08-08
 */
public interface DfLossColourConfigureService extends IService<DfLossColourConfigure> {

    IPage<DfLossColourConfigure> listJoinPage(IPage<DfLossColourConfigure> page, @Param(Constants.WRAPPER)Wrapper<DfLossColourConfigure> wrapper);

}
