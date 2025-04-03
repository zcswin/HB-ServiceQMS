package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfKnifeUseInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.Rate3;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 刀具使用信息 服务类
 * </p>
 *
 * @author guangyao
 * @since 2023-11-13
 */
public interface DfKnifeUseInfoService extends IService<DfKnifeUseInfo> {
    List<Rate3> getKnifeLifeDistribution(@Param(Constants.WRAPPER) Wrapper<DfKnifeUseInfo> wrapper);
}
