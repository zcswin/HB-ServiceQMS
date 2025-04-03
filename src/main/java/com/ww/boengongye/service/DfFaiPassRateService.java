package com.ww.boengongye.service;

import com.ww.boengongye.entity.DfFaiPassRate;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首检通过率 服务类
 * </p>
 *
 * @author zhao
 * @since 2023-03-04
 */
public interface DfFaiPassRateService extends IService<DfFaiPassRate> {

    DfFaiPassRate getRate(String factory, String process, String project, String linebody, String dayOrNight,
                                 String startDate, String endDate, Integer testType);

}
