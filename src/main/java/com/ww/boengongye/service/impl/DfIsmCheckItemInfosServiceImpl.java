package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.boengongye.entity.*;
import com.ww.boengongye.mapper.DfIsmCheckItemInfosMapper;
import com.ww.boengongye.service.DfIsmCheckItemInfosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * ISM测量 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-09-11
 */
@Service
public class DfIsmCheckItemInfosServiceImpl extends ServiceImpl<DfIsmCheckItemInfosMapper, DfIsmCheckItemInfos> implements DfIsmCheckItemInfosService {

	@Autowired
	private DfIsmCheckItemInfosMapper dfIsmCheckItemInfosMapper;

	@Override
	public List<Rate3> listOkRateGroupByDate(QueryWrapper<DfIsmCheckItemInfos> qw) {
		return dfIsmCheckItemInfosMapper.listOkRateGroupByDate(qw);
	}

	@Override
	public Rate4 listIsmNum(QueryWrapper<DfIsmCheckItemInfos> qw) {
		return dfIsmCheckItemInfosMapper.listIsmNum(qw);
	}

	@Override
	public List<Rate3> listAllNumGroupByDate(QueryWrapper<DfLeadDetail> qw) {
		return dfIsmCheckItemInfosMapper.listAllNumGroupByDate(qw);
	}

	@Override
	public List<DfLeadCheckItemInfos> listItemInfosJoinDetail(QueryWrapper<DfIsmCheckItemInfos> qw) {
		return null;
	}

	@Override
	public List<Rate3> listWorkPositionOKRate(QueryWrapper<DfIsmCheckItemInfos> qw) {
		return dfIsmCheckItemInfosMapper.listWorkPositionOKRate(qw);
	}

	@Override
	public List<Rate3> listMachineOneAndMutilOkRate(QueryWrapper<DfIsmCheckItemInfos> qw) {
		return dfIsmCheckItemInfosMapper.listMachineOneAndMutilOkRate(qw);
	}

	@Override
	public List<Rate3> listDateOneAndMutilOkRate(QueryWrapper<DfTzDetail> qw) {
		return dfIsmCheckItemInfosMapper.listDateOneAndMutilOkRate(qw);
	}
}
