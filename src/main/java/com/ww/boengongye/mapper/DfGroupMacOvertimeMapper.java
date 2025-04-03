package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfGroupMacOvertime;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ww.boengongye.entity.DfSizeNgRate;
import com.ww.boengongye.entity.Rate3;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 小组机台超时率 Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2023-03-02
 */
public interface DfGroupMacOvertimeMapper extends BaseMapper<DfGroupMacOvertime> {

    @Select("select gp.respon, inthe_time, 1 - avg(overtime_rate) as overtime_rate " +
            "from df_group_mac_overtime ot " +
            "left join df_group gp on ot.group_id = gp.id " +
            "${ew.customSqlSegment} " +
            "group by group_id, inthe_time")
    List<DfGroupMacOvertime> listOverTimeRate(@Param(Constants.WRAPPER) Wrapper<DfSizeNgRate> wrapper);

    @Select("select gp.respon, inthe_time, avg(overtime_rate) as overtime_rate " +
            "from df_group_mac_overtime ot " +
            "left join df_group gp on ot.group_id = gp.id " +
            "${ew.customSqlSegment} " +
            "group by group_id, inthe_time")
    List<DfGroupMacOvertime> listOverTimeRate2(@Param(Constants.WRAPPER) Wrapper<DfSizeNgRate> wrapper);

    @Select("SELECT gro.linebody str1, concat('h',replace(inthe_time, '-', 'h')) str2, sum(overtime_mac_num) inte1, sum(all_mac_num) inte2, sum(overtime_mac_num) / sum(all_mac_num) dou1 FROM `df_group_mac_overtime` ove " +
            "left join df_group gro on ove.group_id = gro.id " +
            "${ew.customSqlSegment} " +
            "group by gro.linebody, inthe_time " +
            "order by gro.linebody")
    List<Rate3> listOverTimeRateGroupByLineBody(@Param(Constants.WRAPPER) Wrapper<DfGroupMacOvertime> wrapper);

    @Select("SELECT gro.respon str1, concat('h',replace(inthe_time, '-', 'h')) str2, sum(overtime_mac_num) inte1, sum(all_mac_num) inte2, sum(overtime_mac_num) / sum(all_mac_num) dou1 FROM `df_group_mac_overtime` ove " +
            "left join df_group gro on ove.group_id = gro.id " +
            "${ew.customSqlSegment} " +
            "group by gro.respon, inthe_time " +
            "order by gro.respon")
    List<Rate3> listOverTimeRateGroupByRespon(@Param(Constants.WRAPPER) Wrapper<DfGroupMacOvertime> wrapper);

    @Select("SELECT gro.respon str1, sum(overtime_mac_num) / sum(all_mac_num) dou1 FROM `df_group_mac_overtime` ove " +
            "left join df_group gro on ove.group_id = gro.id " +
            "${ew.customSqlSegment} " +
            "group by gro.respon " +
            "order by dou1 desc ")
    List<Rate3> listOverTimeRateGroupByResponTop(@Param(Constants.WRAPPER) Wrapper<DfGroupMacOvertime> wrapper);
}
