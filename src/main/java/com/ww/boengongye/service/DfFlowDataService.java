package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.DfAuditDetail;
import com.ww.boengongye.entity.DfFlowData;
import com.ww.boengongye.utils.Result;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 流程数据 服务类
 * </p>
 *
 * @author zhao
 * @since 2022-10-27
 */
public interface DfFlowDataService extends IService<DfFlowData> {
    IPage<DfFlowData> listBacklog(IPage<DfFlowData> page, @Param(Constants.WRAPPER) Wrapper<DfFlowData> wrapper);

    IPage<DfFlowData> listHaveDone(IPage<DfFlowData> page, @Param(Constants.WRAPPER) Wrapper<DfFlowData> wrapper);

    IPage<DfFlowData> listOvertime(IPage<DfFlowData> page, @Param(Constants.WRAPPER) Wrapper<DfFlowData> wrapper);

    DfFlowData getBacklogCount( @Param(Constants.WRAPPER) Wrapper<DfFlowData> wrapper);

    DfFlowData getHaveDoneCount( @Param(Constants.WRAPPER) Wrapper<DfFlowData> wrapper);

    IPage<DfFlowData> listJoinAudit(IPage<DfFlowData> page, @Param(Constants.WRAPPER) Wrapper<DfFlowData> wrapper);


    IPage<DfFlowData> listByMan(IPage<DfFlowData> page, @Param(Constants.WRAPPER) Wrapper<DfFlowData> wrapper);

    DfAuditDetail getJoinAudit(@Param(Constants.WRAPPER) Wrapper<DfFlowData> wrapper);

    int updateOverTimeById(int id);

    List<DfFlowData> listJoinUser(@Param(Constants.WRAPPER) Wrapper<DfFlowData> wrapper);

    List<DfFlowData> listOverTimeLevelUp(@Param(Constants.WRAPPER) Wrapper<DfFlowData> wrapper);

    List<DfFlowData> listUpOutTimeLevelByAccount(String account);

    IPage<DfFlowData> listOvertimeByMatter(IPage<DfFlowData> page, @Param(Constants.WRAPPER) Wrapper<DfFlowData> wrapper);

    List<DfFlowData> listUpOutTimeLevelByUserName(String userName);

    IPage<DfFlowData> listClosedByUserId(IPage<DfFlowData> page, String userId);

    boolean createFlowData(String title,String type,int dataId);

    public Result createFlowDataFileUpdate(String fileName,String type,Integer dataId);
}
