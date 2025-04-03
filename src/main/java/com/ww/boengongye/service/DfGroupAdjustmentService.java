package com.ww.boengongye.service;

import com.ww.boengongye.entity.DfGroupAdjustment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 小组调机能力 服务类
 * </p>
 *
 * @author zhao
 * @since 2023-03-04
 */
public interface DfGroupAdjustmentService extends IService<DfGroupAdjustment> {

    List<DfGroupAdjustment> listBestRate(String factory, String process, String project, String linebody, String dayOrNight,
                                         String startDate, String endDate, Integer testType);

}
