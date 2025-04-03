package com.ww.boengongye.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfQmsIpqcFlawLogDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhao
 * @since 2024-06-21
 */
public interface DfQmsIpqcFlawLogDetailService extends IService<DfQmsIpqcFlawLogDetail> {

	List<Map> exportRfidDetail(@Param(Constants.WRAPPER) QueryWrapper<DfQmsIpqcFlawLogDetail> ew);
}
