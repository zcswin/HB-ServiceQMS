package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfMalfunctionRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 设备故障记录 服务类
 * </p>
 *
 * @author zhao
 * @since 2022-11-22
 */
public interface DfMalfunctionRecordService extends IService<DfMalfunctionRecord> {
    IPage<DfMalfunctionRecord> listJoinIds(IPage<DfMalfunctionRecord> page, @Param(Constants.WRAPPER) Wrapper<DfMalfunctionRecord> wrapper);
}
