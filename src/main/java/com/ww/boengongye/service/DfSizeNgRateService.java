package com.ww.boengongye.service;

import com.ww.boengongye.entity.DfSizeNgRate;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 尺寸NG率 服务类
 * </p>
 *
 * @author zhao
 * @since 2023-03-05
 */
public interface DfSizeNgRateService extends IService<DfSizeNgRate> {

    DfSizeNgRate getAvg(String factory, String process, String project, String linebody, String dayOrNight,
                        String startDate, String endDate);

}
