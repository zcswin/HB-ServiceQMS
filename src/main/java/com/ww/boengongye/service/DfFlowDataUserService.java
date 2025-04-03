package com.ww.boengongye.service;

import com.ww.boengongye.entity.DfFlowDataUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhao
 * @since 2022-10-28
 */
public interface DfFlowDataUserService extends IService<DfFlowDataUser> {
    List<DfFlowDataUser> insertDataFromOpinion(String startTime, String endTime);

}
