package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfFurnaceTemperature;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.Rate3;
import com.ww.boengongye.utils.Excelable;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 炉温表 服务类
 * </p>
 *
 * @author guangyao
 * @since 2023-10-16
 */
public interface DfFurnaceTemperatureService extends IService<DfFurnaceTemperature>{
    List<String> getFurnaceNameList(@Param(Constants.WRAPPER) Wrapper<DfFurnaceTemperature> wrapper);

    List<Rate3> getOneFurnaceCheckValueList(@Param(Constants.WRAPPER)Wrapper<DfFurnaceTemperature> wrapper, @Param("ew_2")Wrapper<DfFurnaceTemperature> wrapper2);
}
