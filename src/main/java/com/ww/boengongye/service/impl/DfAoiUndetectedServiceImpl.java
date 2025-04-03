package com.ww.boengongye.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfAoiDefect;
import com.ww.boengongye.entity.DfAoiUndetected;
import com.ww.boengongye.mapper.DfAoiUndetectedMapper;
import com.ww.boengongye.service.DfAoiUndetectedService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * AOI漏检记录表 服务实现类
 * </p>
 *
 * @author zhao
 * @since 2023-08-10
 */
@Service
public class DfAoiUndetectedServiceImpl extends ServiceImpl<DfAoiUndetectedMapper, DfAoiUndetected> implements DfAoiUndetectedService {
	@Autowired
	private DfAoiUndetectedMapper dfAoiUndetectedMapper;

	@Override
	public List<Map<String, Object>> fqcNgTopRate(QueryWrapper<DfAoiDefect> ew, Integer top) {
		return dfAoiUndetectedMapper.fqcNgTopRate(ew,top);
	}
}
