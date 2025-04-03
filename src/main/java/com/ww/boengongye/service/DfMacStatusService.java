package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfMacStatus;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.DfMacStatusSize;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhao
 * @since 2022-08-04
 */
public interface DfMacStatusService extends IService<DfMacStatus> {
    List<DfMacStatus> listStatus(@Param(Constants.WRAPPER) Wrapper<DfMacStatus> wrapper);

    List<DfMacStatus> countByStatus();

    Integer updateStatus(String content);

    List<DfMacStatus> listJoinCode(@Param(Constants.WRAPPER) Wrapper<DfMacStatus> wrapper);
}
