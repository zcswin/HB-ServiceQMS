package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DailyCheckSummary;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DailyCheckSummaryMapper extends BaseMapper<DailyCheckSummary> {
    @Select("<script>" +
            " SELECT  da.id,da.date, da.f_time, " +
            " da.sort AS sort, F_BIGPRO, f_seq, F_FAC, F_COLOR, f_test_category, " +
            " F_TEST_MAN, F_SORT, f_stage, f_test_type, f_mac, f_Type, f_line, num, " +
            " spot_check_count, ok_Num, ok_Rate,ng_Num,ng_Rate, shift " +
            " FROM daily_summary da " +
            " ${ew.customSqlSegment} " +
            " ORDER BY da.id " +
            " LIMIT #{pageOffset}, #{pageSize} " +
            "</script>")
    List<DailyCheckSummary> listDailyCheckSummaryPage(
            @Param("ew") QueryWrapper<DailyCheckSummary> ew,
            @Param("pageOffset") int pageOffset,
            @Param("pageSize") int pageSize
    );

    @Select("SELECT COUNT(*) FROM daily_check_summary da  ${ew.customSqlSegment}")
    int Count(@Param(Constants.WRAPPER) QueryWrapper<DailyCheckSummary> qw);


}
