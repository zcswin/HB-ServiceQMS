package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfControlStandardConfig;
import com.ww.boengongye.entity.DfMacResOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 机台-工单关系 服务类
 * </p>
 *
 * @author zhao
 * @since 2023-03-12
 */
public interface DfMacResOrderService extends IService<DfMacResOrder> {
    List<DfMacResOrder> listAllJoinWorkOrder(@Param(Constants.WRAPPER) Wrapper<DfMacResOrder> wrapper);

}
