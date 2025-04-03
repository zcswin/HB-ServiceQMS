package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfAdMacOkRate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ww.boengongye.entity.DfMacYieldData;
import com.ww.boengongye.entity.DfSizeDetail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 员工调机排名--机台良率 Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2023-03-28
 */
public interface DfAdMacOkRateMapper extends BaseMapper<DfAdMacOkRate> {

    @Select("select machine_code, count(machine_code) as num_test, " +
            "sum(if(result = 'OK',1,0)) num_ok, sum(if(result = 'OK',1,0)) / count(machine_code) rate_ok," +
            "factory, project, process, linebody, day_or_night " +
            "from df_size_detail " +
            "${ew.customSqlSegment} " +
            "group by machine_code, factory, project, process, linebody, day_or_night")
    List<DfAdMacOkRate> listOkRateBySizeDetail(@Param(Constants.WRAPPER) Wrapper<DfAdMacOkRate> wrapper);

    @Select("select machine_code, 1-avg(rate_ok) rate_ok " +
            "from df_ad_mac_ok_rate " +
            "${ew.customSqlSegment} " +
            "group by machine_code " +
            "order by rate_ok desc " +
            "limit 20")
    List<DfAdMacOkRate> listOkRateTop10(@Param(Constants.WRAPPER) Wrapper<DfAdMacOkRate> wrapper);

    @Select("SELECT f_mac as machine_code, f_bigpro as project, f_fac as factory, f_seq as process, f_line as linebody,  " +
            "sum(if(status != 'OK', 0, 1)) num_ok, " +
            "count(*) num_test, " +
            "sum(if(status != 'OK', 0, 1)) / count(*) rate_ok " +
            "FROM `df_qms_ipqc_waig_total` " +
            "${ew.customSqlSegment} " +
            "group by f_mac, f_bigpro, f_fac, f_seq, f_line")
    List<DfAdMacOkRate> listAppearOkRate(@Param(Constants.WRAPPER) Wrapper<DfAdMacOkRate> wrapper);
}
