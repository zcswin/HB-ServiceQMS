package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfBcLinker;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * BC-linker表 Mapper 接口
 * </p>
 *
 * @author guangyao
 * @since 2023-09-09
 */
public interface DfBcLinkerMapper extends BaseMapper<DfBcLinker> {


    @Select("select date_format(date_sub(check_time,interval 7 hour),'%c/%e') checkTime\n" +
            "from df_bc_linker\n" +
            "${ew.customSqlSegment} " +
            "group by date_format(date_sub(check_time,interval 7 hour),'%c/%e')")
    List<String> getTimeList(@Param(Constants.WRAPPER)Wrapper<String> wrapper);

    @Select("select time_new.checkTime,df_bc_linker_new.checkNumber\n" +
            "from \n" +
            "\t(select date_format(date_sub(check_time,interval 7 hour),'%c/%e') checkTime\n" +
            "\tfrom df_bc_linker\n" +
            "${ew.customSqlSegment} " +
            "\tgroup by date_format(date_sub(check_time,interval 7 hour),'%c/%e')\n" +
            "\t)time_new\n" +
            "left join\n" +
            "\t(select date_format(check_time,'%c/%e') checkTime,count(0) checkNumber\n" +
            "\tfrom df_bc_linker\n" +
            "${ew.customSqlSegment} " +
            "\tand (time(check_time)>='07:00:00'and time(check_time)<='19:00:00')\n" +
            "\tgroup by date_format(check_time,'%c/%e')\n" +
            "\t)df_bc_linker_new\n" +
            "on df_bc_linker_new.checkTime = time_new.checkTime")
    List<DfBcLinker> getDayDataList(@Param(Constants.WRAPPER)Wrapper<DfBcLinker> wrapper);

    @Select("select time_new.checkTime,df_bc_linker_new.checkNumber\n" +
            "from \n" +
            "\t(select date_format(date_sub(check_time,interval 7 hour),'%c/%e') checkTime\n" +
            "\tfrom df_bc_linker\n" +
            "${ew.customSqlSegment} " +
            "\tgroup by date_format(date_sub(check_time,interval 7 hour),'%c/%e')\n" +
            "\t)time_new\n" +
            "left join\n" +
            "\t(select date_format(date_sub(check_time,interval 7 hour),'%c/%e') checkTime,count(0) checkNumber\n" +
            "\tfrom df_bc_linker\n" +
            "${ew.customSqlSegment} " +
            "\tand (time(check_time)<='07:00:00' or time(check_time)>='19:00:00')\n" +
            "\tgroup by date_format(date_sub(check_time,interval 7 hour),'%c/%e')\n" +
            "\t)df_bc_linker_new\n" +
            "on df_bc_linker_new.checkTime = time_new.checkTime")
    List<DfBcLinker> getNightDataList(@Param(Constants.WRAPPER)Wrapper<DfBcLinker> wrapper);

    @Select("select `position`,sum(if(cipher_result='NG',1,0)),sum(if(trace_card='NG',1,0)),count(0) checkNumber \n" +
            ",FORMAT(sum(if(cipher_result='NG',1,0))/count(0)*100,2) cipherPoint\n" +
            ",FORMAT(sum(if(trace_card='NG',1,0))/count(0)*100,2) traceCardPoint\n" +
            "from df_bc_linker \n" +
            "${ew.customSqlSegment} " +
            "group by `position` \n" +
            "order by `position` asc;")
    List<DfBcLinker> getCipherAndTraceCardPointList(@Param(Constants.WRAPPER) Wrapper<DfBcLinker> wrapper);


    @Select("with dbl_new as(\n" +
            "\tselect \n" +
            "\tsum(if(radium_result='NG',1,0)) radium_result\n" +
            "\t,sum(if(trace_card ='NG',1,0)) trace_card\n" +
            "\t,sum(if(get2D_result ='NG',1,0)) get2D_result\n" +
            "\t,sum(if(cipher_result ='NG',1,0)) cipher_result\n" +
            "\t,sum(if(silo like '%NG%',1,0)) silo\n" +
            "\t,sum(if(network_result ='NG',1,0)) network_result\n" +
            "\t,sum(if(ism_result ='NG',1,0)) ism_result\n" +
            "\t,count(0) checkNumber \n" +
            "\tfrom df_bc_linker \n" +
            "${ew.customSqlSegment} " +
            "),dbl_new2 as(\n" +
            "\tselect 'radium_result' defectName,'扫码不良' defectZHName,radium_result defectNumber,checkNumber from dbl_new\n" +
            "\tunion all\n" +
            "\tselect 'trace_card' defectName,'卡站' defectZHName,trace_card defectNumber,checkNumber from dbl_new\n" +
            "\tunion all\n" +
            "\tselect 'get2D_result' defectName,'2D码扫不上' defectZHName,get2D_result defectNumber,checkNumber from dbl_new\n" +
            "\tunion all\n" +
            "\tselect 'cipher_result' defectName,'读不出码' defectZHName,cipher_result defectNumber,checkNumber from dbl_new\n" +
            "\tunion all\n" +
            "\tselect 'silo' defectName,'分料料仓' defectZHName,silo defectNumber,checkNumber from dbl_new\n" +
            "\tunion all\n" +
            "\tselect 'network_result' defectName,'网络校验' defectZHName,network_result defectNumber,checkNumber from dbl_new\n" +
            "\tunion all\n" +
            "\tselect 'ism_result' defectName,'回捞ISM' defectZHName,ism_result defectNumber,checkNumber from dbl_new\n" +
            ")\n" +
            "select *,FORMAT(defectNumber/checkNumber*100,2) defectPoint \n" +
            "from dbl_new2\n" +
            "order by defectNumber desc\n" +
            "limit 3")
    List<DfBcLinker> getDefectPointTop3List(@Param(Constants.WRAPPER)Wrapper<DfBcLinker> wrapper);

}
