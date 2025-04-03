package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfTzCheckItemInfos;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.DfTzDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * TZ明细表 服务类
 * </p>
 *
 * @author guangyao
 * @since 2023-09-11
 */
public interface DfTzCheckItemInfosService extends IService<DfTzCheckItemInfos> {
//    List<DfTzCheckItemInfos> getDetailData(@Param(Constants.WRAPPER) QueryWrapper<DfTzDetail> wrapper);
}
