package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ww.boengongye.entity.DfEmdDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.boengongye.entity.DfTzDetail;
import com.ww.boengongye.entity.Rate3;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhao
 * @since 2023-09-15
 */
public interface DfEmdDetailService extends IService<DfEmdDetail> {

	List<Rate3> listDateOneAndMutilOkRate(QueryWrapper<DfTzDetail> qw);
}
