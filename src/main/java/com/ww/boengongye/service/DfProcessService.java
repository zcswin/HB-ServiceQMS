package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.DfProcess;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 工序 服务类
 * </p>
 *
 * @author zhao
 * @since 2022-09-19
 */
public interface DfProcessService extends IService<DfProcess> {
    List<DfProcess> listByRouting(@Param("id")int id);

    IPage<DfProcess> listJoinIds(IPage<DfProcess> page, @Param(Constants.WRAPPER) Wrapper<DfProcess> wrapper);

    List<DfProcess> listMacProcessStatus();

    List<DfProcess> listDfProcess( @Param(Constants.WRAPPER) Wrapper<DfProcess> wrapper);
}
