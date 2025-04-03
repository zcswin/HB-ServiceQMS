package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfOrtChangeSpecification;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * ORT规格变更 Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2023-08-23
 */
public interface DfOrtChangeSpecificationMapper extends BaseMapper<DfOrtChangeSpecification> {

	@Select("select * from df_ort_change_specification" +
			"${ew.customSqlSegment}")
	List<Map<String, Object>> listByExport(@Param(Constants.WRAPPER) QueryWrapper<DfOrtChangeSpecification> qw);
}
