package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfTzCheckItemInfos;
import com.ww.boengongye.entity.DfTzCheckItemInfosCheck;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.DfTzDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * TZ测量明细表 服务类
 * </p>
 *
 * @author zhao
 * @since 2025-01-09
 */
public interface DfTzCheckItemInfosCheckService extends IService<DfTzCheckItemInfosCheck> {
    List<DfTzCheckItemInfosCheck> getDetailData(@Param(Constants.WRAPPER) QueryWrapper<DfTzCheckItemInfosCheck> wrapper);
}
