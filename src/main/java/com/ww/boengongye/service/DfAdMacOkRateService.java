package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfAdMacOkRate;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.DfSizeDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 员工调机排名--机台良率 服务类
 * </p>
 *
 * @author zhao
 * @since 2023-03-28
 */
public interface DfAdMacOkRateService extends IService<DfAdMacOkRate> {

    List<DfAdMacOkRate> listOkRateBySizeDetail(@Param(Constants.WRAPPER) Wrapper<DfAdMacOkRate> wrapper);

    List<DfAdMacOkRate> listOkRateTop10(@Param(Constants.WRAPPER) Wrapper<DfAdMacOkRate> wrapper);

    List<DfAdMacOkRate> listAppearOkRate(@Param(Constants.WRAPPER) Wrapper<DfAdMacOkRate> wrapper);

}
