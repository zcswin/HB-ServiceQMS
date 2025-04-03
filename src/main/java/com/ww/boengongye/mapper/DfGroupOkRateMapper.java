package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfGroupMacOvertime;
import com.ww.boengongye.entity.DfGroupOkRate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface DfGroupOkRateMapper extends BaseMapper<DfGroupOkRate> {

    @Select("SELECT gro.respon str1, concat('h',replace(inthe_time, '-', 'h')) str2, sum(ok_check_num) inte1, sum(all_check_num) inte2,sum(ok_check_num) / sum(all_check_num) dou1 FROM `df_group_ok_rate` okr " +
            "left join df_group gro on okr.group_id = gro.id " +
            "${ew.customSqlSegment} " +
            "group by gro.respon, inthe_time " +
            "order by gro.respon")
    List<Rate3> listGroupOkRate(@Param(Constants.WRAPPER) Wrapper<DfGroupOkRate> wrapper);

    @Select("SELECT gro.respon str1, sum(ok_check_num) / sum(all_check_num) dou1 FROM `df_group_ok_rate` okr " +
            "left join df_group gro on okr.group_id = gro.id " +
            "${ew.customSqlSegment} " +
            "group by gro.respon " +
            "order by dou1 ")
    List<Rate3> listGroupOkRateTop(@Param(Constants.WRAPPER) Wrapper<DfGroupOkRate> wrapper);
}
