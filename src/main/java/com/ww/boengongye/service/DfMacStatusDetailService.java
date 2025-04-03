package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfMacStatusDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.DfMacYieldData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhao
 * @since 2022-08-04
 */
public interface DfMacStatusDetailService extends IService<DfMacStatusDetail> {

    List<Integer> deleteTimeOut();

    List<DfMacStatusDetail> listNormalStatus();

    List<DfMacStatusDetail> listWarningStatus();

    List<DfMacStatusDetail> listInsertMac(@Param(Constants.WRAPPER) Wrapper<DfMacStatusDetail> wrapper);
    List<DfMacStatusDetail> listJoinCode(@Param(Constants.WRAPPER) Wrapper<DfMacStatusDetail> wrapper);
}
