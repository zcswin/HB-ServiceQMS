package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfPrintAoiCheck;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.DfPrintAoiCheckDetail;
import com.ww.boengongye.entity.Rate3;
import com.ww.boengongye.utils.Excelable;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 移印AOI检测 服务类
 * </p>
 *
 * @author guangyao
 * @since 2023-09-13
 */
public interface DfPrintAoiCheckService extends IService<DfPrintAoiCheck>, Excelable {

    List<DfPrintAoiCheck> getPassPointList(@Param(Constants.WRAPPER) Wrapper<DfPrintAoiCheck> wrapper, @Param("ew_2")Wrapper<DfPrintAoiCheck> wrapper2);

    List<Rate3> getHolePossPoint(@Param(Constants.WRAPPER)Wrapper<DfPrintAoiCheck> wrapper);

    List<Rate3> getHoleDefectPointTop3(@Param(Constants.WRAPPER)Wrapper<DfPrintAoiCheck> wrapper,@Param("ew_2")Wrapper<DfPrintAoiCheckDetail> wrapper2);
    //优化
    List<Rate3> getAllHoleDefectPointTop3(@Param(Constants.WRAPPER)Wrapper<DfPrintAoiCheck> wrapper);

    List<Rate3> getNgPointList(@Param(Constants.WRAPPER)Wrapper<DfPrintAoiCheck> wrapper,@Param("ew_2")Wrapper<DfPrintAoiCheckDetail> wrapper2);

    List<Rate3> getOnePassPointList(@Param(Constants.WRAPPER)Wrapper<DfPrintAoiCheck> wrapper,@Param("ew_2")Wrapper<DfPrintAoiCheckDetail> wrapper2);

    List<String> getAllHoleList();

    List<DfPrintAoiCheckDetail> listItemInfosJoinDetail(@Param(Constants.WRAPPER)Wrapper<DfPrintAoiCheckDetail> wrapper);

    Rate3 getMinLslAndMaxUsl(@Param(Constants.WRAPPER)Wrapper<DfPrintAoiCheckDetail> wrapper);
}
