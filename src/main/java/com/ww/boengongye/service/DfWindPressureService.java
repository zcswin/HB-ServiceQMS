package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfWindPressure;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.Rate3;
import com.ww.boengongye.utils.Excelable;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 风压点检表 服务类
 * </p>
 *
 * @author guangyao
 * @since 2023-09-18
 */
public interface DfWindPressureService extends IService<DfWindPressure>{
    List<Rate3> getOneSpotWindPressureList(@Param(Constants.WRAPPER) Wrapper<DfWindPressure> wrapper, @Param("ew_2")Wrapper<String> wrapper2);
}
