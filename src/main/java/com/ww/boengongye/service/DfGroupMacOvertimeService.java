package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfGroupMacOvertime;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.DfSizeNgRate;
import com.ww.boengongye.entity.Rate3;
import org.apache.ibatis.annotations.Param;

import java.text.ParseException;
import java.util.List;

/**
 * <p>
 * 小组机台超时率 服务类
 * </p>
 *
 * @author zhao
 * @since 2023-03-02
 */
public interface DfGroupMacOvertimeService extends IService<DfGroupMacOvertime> {

    List<DfGroupMacOvertime> listOvertimeRate(String factory, String process, String project, String linebody, String dayOrNight,
                                              String startDate, String endDate, Integer testType) throws ParseException;

    List<DfGroupMacOvertime> listOvertimeRate2(String factory, String process, String project, String linebody, String dayOrNight,
                                              String startDate, String endDate, Integer testType) throws ParseException;

    List<Rate3> listOverTimeRateGroupByLineBody(@Param(Constants.WRAPPER) Wrapper<DfGroupMacOvertime> wrapper);

    List<Rate3> listOverTimeRateGroupByRespon(@Param(Constants.WRAPPER) Wrapper<DfGroupMacOvertime> wrapper);

    List<Rate3> listOverTimeRateGroupByResponTop(@Param(Constants.WRAPPER) Wrapper<DfGroupMacOvertime> wrapper);

}
