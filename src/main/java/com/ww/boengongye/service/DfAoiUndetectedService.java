package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfAoiDefect;
import com.ww.boengongye.entity.DfAoiUndetected;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * AOI漏检记录表 服务类
 * </p>
 *
 * @author zhao
 * @since 2023-08-10
 */
public interface DfAoiUndetectedService extends IService<DfAoiUndetected> {

	/**
	 * 通用接口查询FQC Top及占比
	 * @param ew
	 * @param top
	 * @return
	 */
	List<Map<String, Object>> fqcNgTopRate(QueryWrapper<DfAoiDefect> ew, Integer top);
}
