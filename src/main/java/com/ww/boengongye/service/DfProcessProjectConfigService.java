package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfProcessProjectConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhao
 * @since 2023-04-26
 */
public interface DfProcessProjectConfigService extends IService<DfProcessProjectConfig> {
    List<DfProcessProjectConfig> listDistinct(@Param(Constants.WRAPPER) Wrapper<DfProcessProjectConfig> wrapper);
}
