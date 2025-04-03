package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfAoiUnitDefect;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 单位缺陷机会数 服务类
 * </p>
 *
 * @author guangyao
 * @since 2023-08-26
 */
public interface DfAoiUnitDefectService extends IService<DfAoiUnitDefect> {
    Integer getUnitNumberByDefectAndProjectId(@Param(Constants.WRAPPER) Wrapper<Integer> wrapper);
}
