package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfQmsIpqcFlawLogDetail;
import com.ww.boengongye.mapper.DfQmsIpqcFlawLogDetailMapper;
import com.ww.boengongye.service.DfQmsIpqcFlawLogDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhao
 * @since 2024-06-21
 */
@Service
public class DfQmsIpqcFlawLogDetailServiceImpl extends ServiceImpl<DfQmsIpqcFlawLogDetailMapper, DfQmsIpqcFlawLogDetail> implements DfQmsIpqcFlawLogDetailService {

	@Autowired
	private DfQmsIpqcFlawLogDetailMapper dfQmsIpqcFlawLogDetailMapper;

	@Override
	public List<Map> exportRfidDetail(@Param(Constants.WRAPPER) QueryWrapper<DfQmsIpqcFlawLogDetail> ew) {
		return dfQmsIpqcFlawLogDetailMapper.exportRfidDetail(ew);
	}
}
