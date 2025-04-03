package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfAoiObaCompare;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.utils.ExportExcelUtil;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * OBA工厂比较 服务类
 * </p>
 *
 * @author guangyao
 * @since 2023-09-04
 */
public interface DfAoiObaCompareService extends IService<DfAoiObaCompare> {

    default void exportModel(HttpServletResponse response, String name){
        ExportExcelUtil exportExcelUtil = new ExportExcelUtil();
        exportExcelUtil.downLoadExcelMould(response, name);
    }

    List<String> getObaFactoryName(@Param(Constants.WRAPPER)Wrapper<String> wrapper);

    List<String> getTimeMonth(@Param(Constants.WRAPPER)Wrapper<String> wrapper);

    List<String> getTimeWeek(@Param(Constants.WRAPPER)Wrapper<String> wrapper);

    List<String> getTimeDay(@Param(Constants.WRAPPER)Wrapper<String> wrapper);

    List<DfAoiObaCompare> getObaPassPointMonth(@Param(Constants.WRAPPER)Wrapper<DfAoiObaCompare> wrapper,@Param("ew_2")Wrapper<String>wrapper2);

    List<DfAoiObaCompare> getObaPassPointWeek(@Param(Constants.WRAPPER)Wrapper<DfAoiObaCompare> wrapper,@Param("ew_2")Wrapper<String>wrapper2);

    List<DfAoiObaCompare> getObaPassPointDay(@Param(Constants.WRAPPER)Wrapper<DfAoiObaCompare> wrapper,@Param("ew_2")Wrapper<String>wrapper2);

    List<DfAoiObaCompare> getObaBatchPassPointMonth(@Param(Constants.WRAPPER)Wrapper<DfAoiObaCompare> wrapper,@Param("ew_2")Wrapper<String>wrapper2);

    List<DfAoiObaCompare> getObaBatchPassPointWeek(@Param(Constants.WRAPPER)Wrapper<DfAoiObaCompare> wrapper,@Param("ew_2")Wrapper<String>wrapper2);

    List<DfAoiObaCompare> getObaBatchPassPointDay(@Param(Constants.WRAPPER)Wrapper<DfAoiObaCompare> wrapper,@Param("ew_2")Wrapper<String>wrapper2);

    List<DfAoiObaCompare> getObaDefectPoint(@Param(Constants.WRAPPER)Wrapper<DfAoiObaCompare> wrapper);

    List<String> getObaDefectNameList(@Param(Constants.WRAPPER)Wrapper<String> wrapper);

    List<DfAoiObaCompare> getObaOneDefectPointList(@Param(Constants.WRAPPER)Wrapper<DfAoiObaCompare> wrapper,@Param("ew_2")Wrapper<DfAoiObaCompare> wrapper2);

    List<String> getObaTypeList(@Param(Constants.WRAPPER)Wrapper<String> wrapper);

    List<DfAoiObaCompare> getObaDefectPointTop5List(@Param(Constants.WRAPPER)Wrapper<DfAoiObaCompare> wrapper);

    List<DfAoiObaCompare> getObaPassPointAndBatchList(@Param(Constants.WRAPPER)Wrapper<DfAoiObaCompare> wrapper);
}
