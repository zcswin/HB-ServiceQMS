package com.ww.boengongye.service;

import com.ww.boengongye.entity.DfSizeItemNgRate;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 尺寸看板汇总表--尺寸NG TOP 服务类
 * </p>
 *
 * @author zhao
 * @since 2023-07-04
 */
public interface DfSizeItemNgRateService extends IService<DfSizeItemNgRate> {

    List<DfSizeItemNgRate> listSizeItemNgRate(String startTime, String endTime);

    List<DfSizeItemNgRate> listAllProcessNgItemNgRate();

}
