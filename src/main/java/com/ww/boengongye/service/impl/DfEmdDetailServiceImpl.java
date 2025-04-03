package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfEmdDetail;
import com.ww.boengongye.entity.DfTzDetail;
import com.ww.boengongye.entity.Rate3;
import com.ww.boengongye.mapper.DfEmdDetailMapper;
import com.ww.boengongye.service.DfEmdDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-09-15
 */
@Service
public class DfEmdDetailServiceImpl extends ServiceImpl<DfEmdDetailMapper, DfEmdDetail> implements DfEmdDetailService {

	@Override
	public List<Rate3> listDateOneAndMutilOkRate(QueryWrapper<DfTzDetail> qw) {
		return null;
	}
}
