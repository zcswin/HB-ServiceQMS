package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.*;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author guangyao
 * @since 2023-11-29
 */
public interface DfQmsIpqcWaigTotalNewService extends IService<DfQmsIpqcWaigTotalNew> {

    DfQmsIpqcWaigTotalNew getTotalAndNgCount(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotalNew> wrapper);

    List<DfQmsIpqcWaigTotalNew> listWaigExcelData(@Param("ew") QueryWrapper<DfQmsIpqcWaigTotalNew> ew);
}
