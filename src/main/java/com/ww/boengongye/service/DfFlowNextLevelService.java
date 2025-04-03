package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfFlowNextLevel;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 流程的下一级配置 服务类
 * </p>
 *
 * @author zhao
 * @since 2022-10-27
 */
public interface DfFlowNextLevelService extends IService<DfFlowNextLevel> {
    List<DfFlowNextLevel> listNextLevel(@Param(Constants.WRAPPER) Wrapper<DfFlowNextLevel> wrapper);
}
