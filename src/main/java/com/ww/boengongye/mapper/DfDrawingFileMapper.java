package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfDrawingFile;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 收集各阶段生产需参考的DFM文件 Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2022-11-25
 */
public interface DfDrawingFileMapper extends BaseMapper<DfDrawingFile> {

	@Select("select * " +
			"from df_drawing_file " +
			"${ew.customSqlSegment }")
	List<Map<String, Object>> listByExport(@Param(Constants.WRAPPER) QueryWrapper<DfDrawingFile> qw);
}
