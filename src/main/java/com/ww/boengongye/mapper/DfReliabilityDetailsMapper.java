package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfReliabilityDetails;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * ORT可靠性明细 Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2023-08-22
 */
public interface DfReliabilityDetailsMapper extends BaseMapper<DfReliabilityDetails> {

	@Select("select * from df_reliability_details" +
			"${ew.customSqlSegment}")
	List<Map<String, Object>> listByExport(@Param(Constants.WRAPPER) QueryWrapper<DfReliabilityDetails> qw);
}
