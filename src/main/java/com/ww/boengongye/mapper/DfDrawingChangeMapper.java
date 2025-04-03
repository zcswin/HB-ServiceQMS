package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfDrawingChange;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 图纸变更 Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2023-08-23
 */
public interface DfDrawingChangeMapper extends BaseMapper<DfDrawingChange> {

	@Select("select * from df_drawing_change" +
			"${ew.customSqlSegment}")
	List<Map<String, Object>> listByExport(@Param(Constants.WRAPPER) QueryWrapper<DfDrawingChange> qw);
}
