package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfMacYieldData;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 机台产量数据 服务类
 * </p>
 *
 * @author zhao
 * @since 2023-01-04
 */
public interface DfMacYieldDataService extends IService<DfMacYieldData> {

    List<DfMacYieldData> listOkRate(@Param(Constants.WRAPPER) Wrapper<DfMacYieldData> wrapper);

    List<DfMacYieldData> listNgRate(@Param(Constants.WRAPPER) Wrapper<DfMacYieldData> wrapper);

    List<DfMacYieldData> listOEE(@Param(Constants.WRAPPER) Wrapper<DfMacYieldData> wrapper);

    List<DfMacYieldData> listPassThroughRate(@Param(Constants.WRAPPER) Wrapper<DfMacYieldData> wrapper);

    List<DfMacYieldData> getMacDetail(@Param(Constants.WRAPPER) Wrapper<DfMacYieldData> wrapper);

    List<DfMacYieldData> getProDetail(@Param(Constants.WRAPPER) Wrapper<DfMacYieldData> wrapper);

}
