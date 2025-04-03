package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfSizeCheckItemInfos;
import com.ww.boengongye.entity.DfSizeCheckItemInfosCheck;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.DfSizeDetail;
import com.ww.boengongye.entity.DfSizeDetailCheck;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhao
 * @since 2025-01-12
 */
public interface DfSizeCheckItemInfosCheckService extends IService<DfSizeCheckItemInfosCheck> {

    List<Map<String,Object>> getDetailData(@Param(Constants.WRAPPER) Wrapper<DfSizeDetailCheck> wrapper);

    List<DfSizeCheckItemInfosCheck> getSizeCheckItemInfosList(@Param(Constants.WRAPPER) Wrapper<DfSizeDetailCheck> wrapper);
}
