package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfGroupClose;
import com.ww.boengongye.entity.DfGroupMacNgRate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
 * @since 2023-08-28
 */
public interface DfGroupMacNgRateMapper extends BaseMapper<DfGroupMacNgRate> {

    @Select("SELECT gro.respon str1, ng.machine_code str2, sum(check_ng_num) / sum(check_all_num) dou1 FROM `df_group_mac_ng_rate` ng " +
            "left join df_group gro on ng.group_id = gro.id " +
            "${ew.customSqlSegment} " +
            "group by gro.respon, ng.machine_code " +
            "order by dou1 desc")
    List<Rate3> listGroupMacNgRateTop(@Param(Constants.WRAPPER) Wrapper<DfGroupMacNgRate> wrapper);
}
