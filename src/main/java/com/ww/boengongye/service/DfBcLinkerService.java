package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.DfBcLinker;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * BC-linker表 服务类
 * </p>
 *
 * @author guangyao
 * @since 2023-09-09
 */
public interface DfBcLinkerService extends IService<DfBcLinker> {

    List<String> getTimeList(@Param(Constants.WRAPPER)Wrapper<String> wrapper);

    List<DfBcLinker> getDayDataList(@Param(Constants.WRAPPER)Wrapper<DfBcLinker> wrapper);

    List<DfBcLinker> getNightDataList(@Param(Constants.WRAPPER)Wrapper<DfBcLinker> wrapper);

    List<DfBcLinker> getCipherAndTraceCardPointList(@Param(Constants.WRAPPER) Wrapper<DfBcLinker> wrapper);

    List<DfBcLinker> getDefectPointTop3List(@Param(Constants.WRAPPER)Wrapper<DfBcLinker> wrapper);
}
