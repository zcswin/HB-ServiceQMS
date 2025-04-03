package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfControlStandardStatus;
import com.ww.boengongye.entity.DfSpotCheckRate;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 点检率表 服务类
 * </p>
 *
 * @author zhao
 * @since 2022-10-14
 */
public interface DfSpotCheckRateService extends IService<DfSpotCheckRate> {

    IPage<DfSpotCheckRate> pageJoinFactory(IPage<DfSpotCheckRate> page, @Param(Constants.WRAPPER) Wrapper<DfSpotCheckRate> wrapper);

}
