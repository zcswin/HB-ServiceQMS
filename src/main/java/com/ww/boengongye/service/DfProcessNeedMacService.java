package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfProcessNeedMac;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 工序需求机台数量表 服务类
 * </p>
 *
 * @author zhao
 * @since 2022-12-20
 */
public interface DfProcessNeedMacService extends IService<DfProcessNeedMac> {
    IPage<DfProcessNeedMac> listJoinIds(IPage<DfProcessNeedMac> page, @Param(Constants.WRAPPER) Wrapper<DfProcessNeedMac> wrapper);
}
