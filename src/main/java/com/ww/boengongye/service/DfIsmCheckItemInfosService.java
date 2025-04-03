package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.*;

import java.util.List;

/**
 * <p>
 * ISM测量 服务类
 * </p>
 *
 * @author zhao
 * @since 2023-09-11
 */
public interface DfIsmCheckItemInfosService extends IService<DfIsmCheckItemInfos> {

	List<Rate3> listOkRateGroupByDate(QueryWrapper<DfIsmCheckItemInfos> qw);

	Rate4 listIsmNum(QueryWrapper<DfIsmCheckItemInfos> qw);

	List<DfLeadCheckItemInfos> listItemInfosJoinDetail(QueryWrapper<DfIsmCheckItemInfos> qw);

	List<Rate3> listAllNumGroupByDate(QueryWrapper<DfLeadDetail> qw);

	List<Rate3> listWorkPositionOKRate(QueryWrapper<DfIsmCheckItemInfos> qw);

	List<Rate3> listMachineOneAndMutilOkRate(QueryWrapper<DfIsmCheckItemInfos> qw);

	List<Rate3> listDateOneAndMutilOkRate(QueryWrapper<DfTzDetail> qw);
}
