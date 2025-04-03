package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfProgrammingChange;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2023-08-22
 */
public interface DfProgrammingChangeMapper extends BaseMapper<DfProgrammingChange> {

	@Select("select " +
			"model" +
			",DATE_FORMAT(regist_time,'%Y-%m-%d %h:%m:%s') regist_time" +
			",test_programming_version" +
			",programming_change_time" +
			",size_drawing_version" +
			",color_drawing_version" +
			",size_drawing_area" +
			",DATE_FORMAT(size_change_time,'%Y-%m-%d %h:%m:%s') size_change_time" +
			",programming_change_reason" +
			",programming_change_content" +
			",programming_import_time" +
			",confirmer " +
			"from df_programming_change " +
			"${ew.customSqlSegment}")
	List<Map<String, Object>> listByExport(@Param(Constants.WRAPPER) QueryWrapper<DfProgrammingChange> qw);
}
