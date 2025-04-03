package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfAoiSeatProtect;
import com.ww.boengongye.entity.DfAoiSeatProtect;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 工位维护 服务类
 * </p>
 *
 * @author guangyao
 * @since 2023-08-04
 */
public interface DfAoiSeatProtectService extends IService<DfAoiSeatProtect> {
    IPage<DfAoiSeatProtect> listJoinPage(IPage<DfAoiSeatProtect> page, @Param(Constants.WRAPPER) Wrapper<DfAoiSeatProtect> wrapper);

    List<DfAoiSeatProtect> getAllList();

    DfAoiSeatProtect getSeatProtectByUserCode(@Param(Constants.WRAPPER) Wrapper<DfAoiSeatProtect> wrapper);
}
