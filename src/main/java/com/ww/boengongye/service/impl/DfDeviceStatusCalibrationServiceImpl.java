package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ww.boengongye.entity.DfDeviceStatusCalibration;
import com.ww.boengongye.mapper.DfDeviceStatusCalibrationMapper;
import com.ww.boengongye.service.DfDeviceStatusCalibrationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 设备状态校准记录 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2022-11-17
 */
@Service
public class DfDeviceStatusCalibrationServiceImpl extends ServiceImpl<DfDeviceStatusCalibrationMapper, DfDeviceStatusCalibration> implements DfDeviceStatusCalibrationService {
    @Autowired
    DfDeviceStatusCalibrationMapper DfDeviceStatusCalibrationMapper;

    @Override
    public IPage<DfDeviceStatusCalibration> listJoinIds(IPage<DfDeviceStatusCalibration> page, Wrapper<DfDeviceStatusCalibration> wrapper) {
        return DfDeviceStatusCalibrationMapper.listJoinIds(page, wrapper);
    }
}
