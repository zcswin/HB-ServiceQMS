package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfSizeOkRate;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 尺寸看板汇总表--尺寸良率汇总 服务类
 * </p>
 *
 * @author zhao
 * @since 2023-08-15
 */
public interface DfSizeOkRateService extends IService<DfSizeOkRate> {

    List<DfSizeOkRate> updateDate();

    List<DfSizeOkRate> listSizeOkRate(@Param(Constants.WRAPPER) Wrapper<DfSizeOkRate> wrapper);

}
