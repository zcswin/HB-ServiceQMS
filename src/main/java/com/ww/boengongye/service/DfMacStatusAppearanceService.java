package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfMacStatusAppearance;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.DfMacStatusSize;
import com.ww.boengongye.entity.DfQmsIpqcWaigTotal;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhao
 * @since 2023-03-13
 */
public interface DfMacStatusAppearanceService extends IService<DfMacStatusAppearance> {

    List<DfMacStatusAppearance> listStatus(@Param(Constants.WRAPPER) Wrapper<DfMacStatusAppearance> wrapper);


    List<DfMacStatusAppearance> countByStatus();

    List<DfQmsIpqcWaigTotal> preparationTimeout(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotal> wrapper);
}
