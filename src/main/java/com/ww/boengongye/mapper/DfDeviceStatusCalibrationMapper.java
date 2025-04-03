package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfDeviceStatusCalibration;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ww.boengongye.entity.DfProcess;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 设备状态校准记录 Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2022-11-17
 */
public interface DfDeviceStatusCalibrationMapper extends BaseMapper<DfDeviceStatusCalibration> {
    @Select("SELECT d.*, fac.factory_name FROM df_device_status_calibration d " +
            "left join df_factory fac on d.factory_code = fac.factory_code " +
            "${ew.customSqlSegment}")
    IPage<DfDeviceStatusCalibration> listJoinIds(IPage<DfDeviceStatusCalibration> page, @Param(Constants.WRAPPER) Wrapper<DfDeviceStatusCalibration> wrapper);
}
