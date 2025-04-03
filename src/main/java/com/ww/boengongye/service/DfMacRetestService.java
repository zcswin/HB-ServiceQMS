package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfMacRetest;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.Rate3;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 机台复测 服务类
 * </p>
 *
 * @author zhao
 * @since 2023-03-05
 */
public interface DfMacRetestService extends IService<DfMacRetest> {

    List<DfMacRetest> listBastPassRate(String factory, String process, String project, String linebody,
                                       String startDate, String endDate, Integer testType);

    List<DfMacRetest> listPoorestMacResponseTime(String factory, String process, String project, String linebody, String dayOrNight,
                               String startDate, String endDate, Integer testType);

    List<Rate3> listDayNightMacOkRate(@Param(Constants.WRAPPER) Wrapper<DfMacRetest> wrapper);

    List<Rate3> listDayNightMacOkRateAndResTimeDetail(@Param(Constants.WRAPPER) Wrapper<DfMacRetest> wrapper);

    List<Rate3> listMacOkRateTop(@Param(Constants.WRAPPER) Wrapper<DfMacRetest> wrapper);

    List<Rate3> listMacResponseTimeTop(@Param(Constants.WRAPPER) Wrapper<DfMacRetest> wrapper);

}
