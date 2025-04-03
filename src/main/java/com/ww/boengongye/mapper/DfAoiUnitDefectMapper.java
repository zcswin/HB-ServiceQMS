package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfAoiUnitDefect;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 单位缺陷机会数 Mapper 接口
 * </p>
 *
 * @author guangyao
 * @since 2023-08-26
 */
public interface DfAoiUnitDefectMapper extends BaseMapper<DfAoiUnitDefect> {

    @Select("select daud.count " +
            "from df_aoi_unit_defect daud " +
            "${ew.customSqlSegment}")
    Integer getUnitNumberByDefectAndProjectId(@Param(Constants.WRAPPER)Wrapper<Integer> wrapper);

}
