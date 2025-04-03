package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfDefectCodeProtect;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 不良代码维护 服务类
 * </p>
 *
 * @author guangyao
 * @since 2023-08-07
 */
public interface DfDefectCodeProtectService extends IService<DfDefectCodeProtect> {
    IPage<DfDefectCodeProtect> listJoinPage(IPage<DfDefectCodeProtect> page, @Param(Constants.WRAPPER) Wrapper<DfDefectCodeProtect> wrapper);

    List<DfDefectCodeProtect> getAllList();
}
