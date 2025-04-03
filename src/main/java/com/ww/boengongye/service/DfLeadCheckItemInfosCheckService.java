package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfLeadCheckItemInfos;
import com.ww.boengongye.entity.DfLeadCheckItemInfosCheck;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.DfSizeDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhao
 * @since 2025-01-09
 */
public interface DfLeadCheckItemInfosCheckService extends IService<DfLeadCheckItemInfosCheck> {
    List<DfLeadCheckItemInfosCheck> getDetailData(@Param(Constants.WRAPPER) QueryWrapper<DfLeadCheckItemInfosCheck> sizeWrapper);
}
