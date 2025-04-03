package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfKnifStatus;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.Rate3;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 砂轮刀具状态 服务类
 * </p>
 *
 * @author zhao
 * @since 2023-09-21
 */
public interface DfKnifStatusService extends IService<DfKnifStatus> {
    List<Rate3> getKnifeLifeDistribution(@Param(Constants.WRAPPER) Wrapper<DfKnifStatus> wrapper);
}
