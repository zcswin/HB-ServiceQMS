package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfGroupClose;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ww.boengongye.entity.DfGroupOkRate;
import com.ww.boengongye.entity.Rate3;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 小组两小时时间段良率 Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2023-08-27
 */
public interface DfGroupCloseMapper extends BaseMapper<DfGroupClose> {

    @Select("select size.machine_code str1,  " +
            "count(*) inte1, " +
            "sum(if(aud.end_time is not null, 1, 0)) inte2 " +
            "from df_audit_detail aud " +
            "left join df_size_detail size on aud.parent_id = size.id " +
            "${ew.customSqlSegment} " +
            "group by machine_code")
    List<Rate3> listMachineCloseNum(@Param(Constants.WRAPPER) Wrapper<DfGroupClose> wrapper);

    @Select("select all_rate.date str1, all_rate.day_or_night str2, little_rate.open_num inte1, little_rate.rate dou1, all_rate.rate dou2 from " +
            "(select * from  " +
            "(SELECT date_format(test_time, '%Y-%m-%d') date, sum(close_num) / sum(open_num) rate FROM `df_group_close` clo " +
            "left join df_group gro on clo.group_id = gro.id " +
            "${ew.customSqlSegment} " +
            "group by date) t1, (select '白班' day_or_night union select '夜班' day_or_night) t2) all_rate " +
            "left join  " +
            "(SELECT date_format(test_time, '%Y-%m-%d') date, day_or_night, sum(open_num) open_num, sum(close_num) / sum(open_num) rate FROM `df_group_close` clo " +
            "left join df_group gro on clo.group_id = gro.id " +
            "${ew.customSqlSegment} " +
            "group by date, day_or_night) little_rate on all_rate.day_or_night = little_rate.day_or_night and all_rate.date = little_rate.date " +
            "order by str1 ")
    List<Rate3> listCloseRate(@Param(Constants.WRAPPER) Wrapper<DfGroupClose> wrapper);

    @Select("SELECT gro.respon str1, sum(close_num) / sum(open_num) dou1 FROM `df_group_close` clo " +
            "left join df_group gro on clo.group_id = gro.id " +
            "${ew.customSqlSegment} " +
            "group by gro.respon " +
            "order by dou1 ")
    List<Rate3> listGroupCloseRateTop(@Param(Constants.WRAPPER) Wrapper<DfGroupClose> wrapper);
}
