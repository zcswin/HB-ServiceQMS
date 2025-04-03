package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfPrintAoiRecheck;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.DfPrintAoiRecheckDetail;
import com.ww.boengongye.utils.Excelable;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 移印AOI人工复查 服务类
 * </p>
 *
 * @author guangyao
 * @since 2023-09-13
 */
public interface DfPrintAoiRecheckService extends IService<DfPrintAoiRecheck>, Excelable {
    List<DfPrintAoiRecheck> getOverkillOrEscapeList(@Param(Constants.WRAPPER) Wrapper<DfPrintAoiRecheck> wrapper);

    List<DfPrintAoiRecheckDetail> getEscapeDetailList(@Param(Constants.WRAPPER)Wrapper<DfPrintAoiRecheckDetail> wrapper);
}
