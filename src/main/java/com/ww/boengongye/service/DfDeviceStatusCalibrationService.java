package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfDeviceStatusCalibration;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 设备状态校准记录 服务类
 * </p>
 *
 * @author zhao
 * @since 2022-11-17
 */
public interface DfDeviceStatusCalibrationService extends IService<DfDeviceStatusCalibration> {
    IPage<DfDeviceStatusCalibration> listJoinIds(IPage<DfDeviceStatusCalibration> page, @Param(Constants.WRAPPER) Wrapper<DfDeviceStatusCalibration> wrapper);
}
