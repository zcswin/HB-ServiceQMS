package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.*;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhao
 * @since 2023-03-11
 */
public interface DfMacStatusSizeService extends IService<DfMacStatusSize> {
    List<DfMacStatusSize> listStatus();
    List<DfMacStatusSize> listStatus2(@Param(Constants.WRAPPER) Wrapper<DfMacStatusSize> wrapper);

    List<DfMacStatusSize> countByStatus();

    List<DfMacStatusSize> listJoinCode(@Param(Constants.WRAPPER) Wrapper<DfMacStatusSize> wrapper);

    List<Rate3> getProcessMacStatusList(@Param(Constants.WRAPPER)Wrapper<DfMacStatusSize> wrapper);

    List<Rate3> getMacStatusInfoList(@Param(Constants.WRAPPER)Wrapper<DfSizeDetail>wrapper);

    List<Rate3> getProcessMacNormalTime(@Param(Constants.WRAPPER)Wrapper<DfSizeMacDuration>wrapper);

    List<Rate3> countSizeMacDurationInfoList(@Param(Constants.WRAPPER)Wrapper<DfSizeMacDuration> wrapper1,@Param("ew_2")Wrapper<DfSizeDetail> wrapper2);
}
