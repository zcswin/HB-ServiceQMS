package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfAdMacOkRate;
import com.ww.boengongye.entity.DfAdSizeNgRate;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 员工调机排名--尺寸NG率 服务类
 * </p>
 *
 * @author zhao
 * @since 2023-03-28
 */
public interface DfAdSizeNgRateService extends IService<DfAdSizeNgRate> {

    List<DfAdSizeNgRate> listNgRateByItemInfos(@Param(Constants.WRAPPER) Wrapper<DfAdSizeNgRate> wrapper);

    List<DfAdSizeNgRate> listNgTop10(@Param(Constants.WRAPPER) Wrapper<DfAdSizeNgRate> wrapper);

    List<DfAdSizeNgRate> listAppearNgInfos(String startTime, String endTime);

}
