package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfAoiSiCompare;
import com.ww.boengongye.entity.DfAoiSiCompare;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * AOI SI工厂对比 服务类
 * </p>
 *
 * @author guangyao
 * @since 2023-09-07
 */
public interface DfAoiSiCompareService extends IService<DfAoiSiCompare> {
    List<String> getSiFactoryName(@Param(Constants.WRAPPER) Wrapper<String> wrapper);

    List<String> getSiSellPlace(@Param(Constants.WRAPPER) Wrapper<String> wrapper);

    List<String> getTimeMonth(@Param(Constants.WRAPPER)Wrapper<String> wrapper);

    List<String> getTimeWeek(@Param(Constants.WRAPPER)Wrapper<String> wrapper);


    List<String> getTimeDay(@Param(Constants.WRAPPER)Wrapper<String> wrapper);

    List<DfAoiSiCompare> getSiPassPointMonth(@Param(Constants.WRAPPER)Wrapper<DfAoiSiCompare> wrapper, @Param("ew_2")Wrapper<String>wrapper2);


    List<DfAoiSiCompare> getSiPassPointWeek(@Param(Constants.WRAPPER)Wrapper<DfAoiSiCompare> wrapper,@Param("ew_2")Wrapper<String>wrapper2);


    List<DfAoiSiCompare> getSiPassPointDay(@Param(Constants.WRAPPER)Wrapper<DfAoiSiCompare> wrapper,@Param("ew_2")Wrapper<String>wrapper2);



    List<DfAoiSiCompare> getSiBatchPassPointMonth(@Param(Constants.WRAPPER)Wrapper<DfAoiSiCompare> wrapper,@Param("ew_2")Wrapper<String>wrapper2);


    List<DfAoiSiCompare> getSiBatchPassPointWeek(@Param(Constants.WRAPPER)Wrapper<DfAoiSiCompare> wrapper,@Param("ew_2")Wrapper<String>wrapper2);


    List<DfAoiSiCompare> getSiBatchPassPointDay(@Param(Constants.WRAPPER)Wrapper<DfAoiSiCompare> wrapper,@Param("ew_2")Wrapper<String>wrapper2);

    List<DfAoiSiCompare> getSiDefectPointFactory(@Param(Constants.WRAPPER)Wrapper<DfAoiSiCompare> wrapper);

    List<String> getSiDefectNameListFactory(@Param(Constants.WRAPPER)Wrapper<String> wrapper);

    List<DfAoiSiCompare> getSiOneDefectPointListFactory(@Param(Constants.WRAPPER)Wrapper<DfAoiSiCompare> wrapper,@Param("ew_2")Wrapper<DfAoiSiCompare> wrapper2);

    List<DfAoiSiCompare> getSiDefectPointSellPlace(@Param(Constants.WRAPPER)Wrapper<DfAoiSiCompare> wrapper);

    List<String> getSiDefectNameListSellPlace(@Param(Constants.WRAPPER)Wrapper<String> wrapper);

    List<DfAoiSiCompare> getSiOneDefectPointListSellPlace(@Param(Constants.WRAPPER)Wrapper<DfAoiSiCompare> wrapper,@Param("ew_2")Wrapper<DfAoiSiCompare> wrapper2);

    List<String> getSiTypeList(@Param(Constants.WRAPPER)Wrapper<String> wrapper);

    List<DfAoiSiCompare> getSiDefectPointTop5List(@Param(Constants.WRAPPER)Wrapper<DfAoiSiCompare> wrapper);

    List<DfAoiSiCompare> getSiPassPointAndBatchList(@Param(Constants.WRAPPER)Wrapper<DfAoiSiCompare> wrapper);

}
