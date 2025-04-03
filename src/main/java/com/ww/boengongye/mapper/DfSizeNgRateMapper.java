package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfControlStandardConfig;
import com.ww.boengongye.entity.DfSizeNgRate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 尺寸NG率 Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2023-03-05
 */
public interface DfSizeNgRateMapper extends BaseMapper<DfSizeNgRate> {

    @Select("SELECT avg(appear_length1) as appear_length1, " +
            "avg(appear_length2) as appear_length2, " +
            "avg(appear_length3) as appear_length3, " +
            "avg(appear_width1) as appear_width1, " +
            "avg(appear_width2) as appear_width2, " +
            "avg(appear_width3) as appear_width3, " +
            "avg(mi_hole_diameter) as mi_hole_diameter, " +
            "avg(mi_hole_roundness) as mi_hole_roundness, " +
            "avg(mi_hole_center_distance_x) as mi_hole_center_distance_x, " +
            "avg(mi_hole_center_distance_y) as mi_hole_center_distance_y, " +
            "avg(db_hole_diameter) as db_hole_diameter, " +
            "avg(db_hole_roundness) as db_hole_roundness, " +
            "avg(db_hole_center_distance_x) as db_hole_center_distance_x, " +
            "avg(db_hole_center_distance_y) as db_hole_center_distance_y, " +
            "avg(s_hole_diameter) as s_hole_diameter, " +
            "avg(s_hole_roundness) as s_hole_roundness, " +
            "avg(s_hole_center_distance_x) as s_hole_center_distance_x, " +
            "avg(s_hole_center_distance_y) as s_hole_center_distance_y, " +
            "avg(mic_hole_diameter) as mic_hole_diameter, " +
            "avg(mic_hole_roundness) as mic_hole_roundness, " +
            "avg(mic_hole_center_distance_x) as mic_hole_center_distance_x, " +
            "avg(mic_hole_center_distance_y) as mic_hole_center_distance_y " +
            "FROM df_size_ng_rate " +
            "${ew.customSqlSegment}")
    DfSizeNgRate getAvg(@Param(Constants.WRAPPER) Wrapper<DfSizeNgRate> wrapper);
}
