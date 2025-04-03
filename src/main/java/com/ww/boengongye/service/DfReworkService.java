package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfRework;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 返工表 服务类
 * </p>
 *
 * @author zhao
 * @since 2022-12-19
 */
public interface DfReworkService extends IService<DfRework> {
    IPage<DfRework> listJoinIds(IPage<DfRework> page, @Param(Constants.WRAPPER) Wrapper<DfRework> wrapper);
}
