package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfFaiPassRate;
import com.ww.boengongye.entity.DfMacRetest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ww.boengongye.entity.Rate3;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 机台复测 Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2023-03-05
 */
public interface DfMacRetestMapper extends BaseMapper<DfMacRetest> {

    @Select("select machine_code, sum(if(retest_result='OK', 1, 0)) / count(machine_code) as pass_rate " +
            "from df_mac_retest " +
            "${ew.customSqlSegment} " +
            "group by machine_code " +
            "order by pass_rate desc " +
            "limit 10")
    List<DfMacRetest> listBestPassRate(@Param(Constants.WRAPPER) Wrapper<DfMacRetest> wrapper);

    @Select("select machine_code, " +
            "avg(response_time) response_time " +
            "from df_mac_retest " +
            "${ew.customSqlSegment} " +
            "group by machine_code " +
            "order by response_time desc " +
            "limit 5")
    List<DfMacRetest> listPoorestMacResponseTime(@Param(Constants.WRAPPER) Wrapper<DfMacRetest> wrapper);

    @Select("select date str1, day_ok_num / day_all_num dou1, night_ok_num / night_all_num dou2, ok_num / all_num dou3 from " +
            "(select DATE_FORMAT(DATE_SUB(ret.create_time, INTERVAL 7 HOUR),'%m-%d') date,  " +
            "sum(if(HOUR(DATE_SUB(ret.create_time, INTERVAL 7 HOUR)) between 0 and 11 and ret.retest_result = 'OK', 1, 0)) day_ok_num,  " +
            "sum(if(HOUR(DATE_SUB(ret.create_time, INTERVAL 7 HOUR)) between 0 and 11, 1, 0)) day_all_num, " +
            "sum(if(HOUR(DATE_SUB(ret.create_time, INTERVAL 7 HOUR)) between 12 and 23 and ret.retest_result = 'OK', 1, 0)) night_ok_num,  " +
            "sum(if(HOUR(DATE_SUB(ret.create_time, INTERVAL 7 HOUR)) between 12 and 23, 1, 0)) night_all_num, " +
            "sum(if(ret.retest_result = 'OK', 1, 0)) ok_num, " +
            "count(*) all_num " +
            "from df_mac_retest ret " +
            "left join df_process pro on ret.machine_code like concat(pro.first_code, '%') " +
            "${ew.customSqlSegment}  " +
            "group by date) t")
    List<Rate3> listDayNightMacOkRate(@Param(Constants.WRAPPER) Wrapper<DfMacRetest> wrapper);

    @Select("select t.date str1, t.machine_code str2, t1.ok_rate dou1, t1.response_time dou2 from  " +
            "(select * from  " +
            "(select distinct DATE_FORMAT(DATE_SUB(ret.create_time, INTERVAL 7 HOUR),'%m-%d') date  " +
            "from df_mac_retest ret " +
            "left join df_process pro on ret.machine_code like concat(pro.first_code, '%') " +
            "${ew.customSqlSegment} ) date, " +
            "(select distinct machine_code " +
            "from df_mac_retest ret " +
            "left join df_process pro on ret.machine_code like concat(pro.first_code, '%') " +
            "${ew.customSqlSegment} ) mac ) t " +
            "left join  " +
            "(select date, machine_code, ok_num / all_num ok_rate, rate.response_time  from " +
            "(select DATE_FORMAT(DATE_SUB(ret.create_time, INTERVAL 7 HOUR),'%m-%d') date,  " +
            "machine_code, " +
            "sum(if(ret.retest_result = 'OK', 1, 0)) ok_num, " +
            "count(*) all_num, " +
            "avg(response_time) response_time " +
            "from df_mac_retest ret " +
            "left join df_process pro on ret.machine_code like concat(pro.first_code, '%') " +
            "${ew.customSqlSegment}   " +
            "group by date, machine_code) rate) t1 on t.date = t1.date and t.machine_code = t1.machine_code " +
            "order by t.machine_code, t.date")
    List<Rate3> listDayNightMacOkRateAndResTimeDetail(@Param(Constants.WRAPPER) Wrapper<DfMacRetest> wrapper);

    @Select("select  " +
            "machine_code str1, " +
            "sum(if(ret.retest_result = 'OK', 1, 0)) / count(*) dou1 " +
            "from df_mac_retest ret " +
            "left join df_process pro on ret.machine_code like concat(pro.first_code, '%') " +
            "${ew.customSqlSegment} " +
            "group by machine_code " +
            "order by dou1")
    List<Rate3> listMacOkRateTop(@Param(Constants.WRAPPER) Wrapper<DfMacRetest> wrapper);

    @Select("select  " +
            "machine_code str1, " +
            "avg(response_time) dou1 " +
            "from df_mac_retest ret " +
            "left join df_process pro on ret.machine_code like concat(pro.first_code, '%') " +
            "${ew.customSqlSegment} " +
            "group by machine_code " +
            "order by dou1 desc")
    List<Rate3> listMacResponseTimeTop(@Param(Constants.WRAPPER) Wrapper<DfMacRetest> wrapper);
}
