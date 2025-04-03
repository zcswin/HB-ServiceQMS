package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfAoiCarProtect;
import com.ww.boengongye.entity.DfAoiMachineInspection;
import com.ww.boengongye.entity.DfAoiMachineProtect;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.DfAoiPassPoint;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * AOI机台维护 服务类
 * </p>
 *
 * @author guangyao
 * @since 2023-08-03
 */
public interface DfAoiMachineProtectService extends IService<DfAoiMachineProtect> {
    IPage<DfAoiMachineProtect> listJoinPage(IPage<DfAoiMachineProtect> page, @Param(Constants.WRAPPER) Wrapper<DfAoiMachineProtect> wrapper);

    List<DfAoiMachineInspection> getAllMachineInspectionList(@Param(Constants.WRAPPER)Wrapper<DfAoiMachineInspection> wrapper);

    List<DfAoiMachineInspection> getMachineAndUserNameList(IPage<DfAoiMachineInspection> page,@Param(Constants.WRAPPER)Wrapper<DfAoiMachineInspection> wrapper);

    List<DfAoiMachineInspection> getMachineInspectionList(@Param(Constants.WRAPPER)Wrapper<DfAoiMachineInspection> wrapper,@Param("ew_2")Wrapper<DfAoiMachineInspection> wrapper2);

    Integer getTotalInputNumber(@Param(Constants.WRAPPER) Wrapper<Integer> wrapper);

    Integer getBackNumber(@Param(Constants.WRAPPER)Wrapper<Integer> wrapper);

    Integer getBackOKNumber(@Param(Constants.WRAPPER)Wrapper<Integer> wrapper);

    List<DfAoiPassPoint> getAllAoiPassPointList(@Param(Constants.WRAPPER)Wrapper<DfAoiPassPoint> wrapper);

    List<DfAoiPassPoint> getAoiPassPointOneList(@Param(Constants.WRAPPER)Wrapper<DfAoiPassPoint> wrapper);

    List<DfAoiPassPoint> getAoiPassPointList(IPage<DfAoiPassPoint> page,@Param(Constants.WRAPPER)Wrapper<DfAoiPassPoint> wrapper);

    List<DfAoiPassPoint> getAoiPassPointWeekOneList(@Param(Constants.WRAPPER)Wrapper<DfAoiPassPoint> wrapper);

    List<DfAoiPassPoint> getAoiPassPointWeekList(@Param(Constants.WRAPPER)Wrapper<DfAoiPassPoint> wrapper);
}
