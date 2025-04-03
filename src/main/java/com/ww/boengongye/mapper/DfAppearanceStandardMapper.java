package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfAppearanceStandard;
import com.ww.boengongye.entity.DfDrawingFile;
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
 * @since 2023-08-21
 */
public interface DfAppearanceStandardMapper extends BaseMapper<DfAppearanceStandard> {

	@Select("select * from df_appearance_standard " +
			"${ew.customSqlSegment}")
	List<Map<String, Object>> listByExport(@Param(Constants.WRAPPER) QueryWrapper<DfDrawingFile> qw) ;
}
