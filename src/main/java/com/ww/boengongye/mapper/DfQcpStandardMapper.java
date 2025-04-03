package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfQcpStandard;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * QCP标准 Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2022-11-30
 */
public interface DfQcpStandardMapper extends BaseMapper<DfQcpStandard> {

	@Select("select * " +
			"from df_qcp_standard" +
			"${ew.customSqlSegment}")
	List<Map<String, Object>> listByExport(@Param(Constants.WRAPPER) QueryWrapper<DfQcpStandard> qw);

}
