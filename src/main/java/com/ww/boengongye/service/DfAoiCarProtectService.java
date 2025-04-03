package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfAoiCarProtect;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 车间维护 服务类
 * </p>
 *
 * @author guangyao
 * @since 2023-08-03
 */
public interface DfAoiCarProtectService extends IService<DfAoiCarProtect> {
    IPage<DfAoiCarProtect> listJoinPage(IPage<DfAoiCarProtect> page, @Param(Constants.WRAPPER) Wrapper<DfAoiCarProtect> wrapper);
}
