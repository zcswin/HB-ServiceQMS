package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfECodeProtect;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * E-Code维护 服务类
 * </p>
 *
 * @author guangyao
 * @since 2023-08-07
 */
public interface DfECodeProtectService extends IService<DfECodeProtect> {
    IPage<DfECodeProtect> listJoinPage(IPage<DfECodeProtect> page, @Param(Constants.WRAPPER) Wrapper<DfECodeProtect> wrapper);


}
