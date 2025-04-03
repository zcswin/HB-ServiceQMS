package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfQmsRfidClampSn;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhao
 * @since 2024-06-24
 */
public interface DfQmsRfidClampSnService extends IService<DfQmsRfidClampSn> {
    List<DfQmsRfidClampSn> listByExport(@Param(Constants.WRAPPER) Wrapper<DfQmsRfidClampSn> wrapper);
}
