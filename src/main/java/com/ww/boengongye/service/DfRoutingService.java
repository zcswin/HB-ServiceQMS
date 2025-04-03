package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfRouting;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhao
 * @since 2022-09-29
 */
public interface DfRoutingService extends IService<DfRouting> {

    List<DfRouting> listJoinProcess(@Param(Constants.WRAPPER) Wrapper<DfRouting> wrapper);

}
