package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfQmsIpqcWaigDetail;
import com.ww.boengongye.entity.DfSizeCheckItemInfos;
import com.ww.boengongye.entity.Rate3;
import com.ww.boengongye.entity.Rate4;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2022-09-16
 */
public interface DfQmsIpqcWaigDetailMapper extends BaseMapper<DfQmsIpqcWaigDetail> {
    @Select("select d.*,c.color,p.f_barcode,p.f_bigpro,p.f_mac" +
            " from df_qms_ipqc_waig_detail d left join df_qms_ipqc_flaw_color c on d.f_sort=c.name  " +
            " left join df_qms_ipqc_waig_total p on d.f_parent_id=p.id  " +
            " join df_process dp ON dp.process_name = p.f_seq  " +
            "${ew.customSqlSegment} ")
    List<DfQmsIpqcWaigDetail> listByJoin(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigDetail> wrapper);


    @Select("select k.f_sm_area,count(k.id) id from (select d.f_sm_area, d.id  from df_qms_ipqc_waig_detail d  join df_qms_ipqc_waig_total p on d.f_parent_id=p.id " +
            "join df_process dp ON dp.process_name = p.f_seq " +
            "${ew.customSqlSegment} ) k  group by k.f_sm_area")
    List<DfQmsIpqcWaigDetail>
    listBySmAreaCount(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigDetail> wrapper);

//    @Selqect("select SUM(spot_check_count) as id from df_qms_ipqc_waig_total join ${ew.customSqlSegment}  ")
    //只查5楼工序
    @Select("select SUM(spot_check_count) as id from df_qms_ipqc_waig_total tol join df_process dp ON dp.process_name = tol.f_seq ${ew.customSqlSegment}  ")
   DfQmsIpqcWaigDetail getSumAffectCount(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigDetail> wrapper);


    @Select({"<script> " +
            "WITH temp_df_size_detail AS(\n" +
            "\tselect count(id) as size_total from df_size_detail t1 where 1 = 1\n" +
            "\tand t1.test_time between #{startTime} and #{endTime}\n" +
            "<if test = 'factoryId != null and factoryId != &quot;&quot;'>" +
            "AND factory = #{factoryId} " +
            "</if>" +
            "<if test = 'process != null and process != &quot;&quot;'>" +
            "AND process = #{process} " +
            "</if>" +
            "<if test = 'lineBody != null and lineBody != &quot;&quot;'>" +
            "AND linebody = #{lineBody} " +
            "</if>" +
            "<if test = 'item != null and item != &quot;&quot;'>" +
            "AND project = #{item} " +
            "</if>" +
            "),\n" +
            "\n" +
            "temp_df_qms_ipqc_waig_total AS(\n" +
            "\tselect sum(spot_check_count) as waig_total from df_qms_ipqc_waig_total t2 where 1 =1 \n" +
            "\tand t2.f_time between #{startTime} and #{endTime}\n" +
            "<if test = 'factoryId != null and factoryId != &quot;&quot;'>" +
            "AND f_fac = #{factoryId} " +
            "</if>" +
            "<if test = 'process != null and process != &quot;&quot;'>" +
            "AND f_seq = #{process} " +
            "</if>" +
            "<if test = 'lineBody != null and lineBody != &quot;&quot;'>" +
            "AND f_line = #{lineBody} " +
            "</if>" +
            "<if test = 'item != null and item != &quot;&quot;'>" +
            "AND f_bigpro = #{item} " +
            "</if>" +
            "),\n" +
            "\n" +
            "temp_df_data AS(\n" +
            "\tselect t.f_sort item_name ,sum(IF(t.f_result = 'fail',1,0)) ng_num\n" +
            "\t,(select size_total from temp_df_size_detail) as size_total\n" +
            "\t,(select waig_total from temp_df_qms_ipqc_waig_total) as waig_total\n" +
            "\tFROM df_qms_ipqc_waig_detail t\n" +
            "\tJOIN df_qms_ipqc_waig_total t2 on t.f_parent_id = t2.id\n" +
            "\twhere 1 = 1\n" +
            "\tand t.f_time between #{startTime} and #{endTime}\n" +
            "<if test = 'factoryId != null and factoryId != &quot;&quot;'>" +
            "AND t2.f_fac = #{factoryId} " +
            "</if>" +
            "<if test = 'process != null and process != &quot;&quot;'>" +
            "AND t2.f_seq = #{process} " +
            "</if>" +
            "<if test = 'lineBody != null and lineBody != &quot;&quot;'>" +
            "AND t2.f_line = #{lineBody} " +
            "</if>" +
            "<if test = 'item != null and item != &quot;&quot;'>" +
            "AND t2.f_bigpro = #{item} " +
            "</if>" +
            "\tGROUP BY f_sort\n" +
            ")\n" +
            "\n" +
            "SELECT *,ng_num / GREATEST(size_total,waig_total) ng_rate\n" +
            "FROM temp_df_data\n" +
            "ORDER BY ng_rate DESC\n" +
            "limit 5 " +
            "</script>"})
	List<DfSizeCheckItemInfos> fullApperanceNGTop5(String factoryId, String process, String lineBody, String item, String startTime, String endTime);


    @Select("select \n" +
            "'ISM' as str1\n" +
            ",format(sum(if(item.`result`='OK',1,0))/count(0)*100,2) dou1\n" +
            "from df_ism_check_item_infos item \n" +
            "${ew_2.customSqlSegment} " +
            "union all\n" +
            "select \n" +
            "'丝印Logo' as str1\n" +
            ",format((sum(dqiwt.spot_check_count)-sum(dqiwt.affect_count))/sum(dqiwt.spot_check_count)*100,2) dou1\n" +
            "from df_qms_ipqc_waig_total dqiwt \n" +
            "where dqiwt.f_seq ='丝印Logo'\n" +
            "and ${ew.sqlSegment} " +
            "union all\n" +
            "select \n" +
            "'丝印一层' as str1\n" +
            ",format((sum(dqiwt.spot_check_count)-sum(dqiwt.affect_count))/sum(dqiwt.spot_check_count)*100,2) dou1\n" +
            "from df_qms_ipqc_waig_total dqiwt \n" +
            "where dqiwt.f_seq ='丝印一层'\n" +
            "and ${ew.sqlSegment} " +
            "union all\n" +
            "select \n" +
            "'丝印二层' as str1\n" +
            ",format((sum(dqiwt.spot_check_count)-sum(dqiwt.affect_count))/sum(dqiwt.spot_check_count)*100,2) dou1\n" +
            "from df_qms_ipqc_waig_total dqiwt \n" +
            "where dqiwt.f_seq ='丝印二层'\n" +
            "and ${ew.sqlSegment} " +
            "union all\n" +
            "select \n" +
            "'丝印三层' as str1\n" +
            ",format((sum(dqiwt.spot_check_count)-sum(dqiwt.affect_count))/sum(dqiwt.spot_check_count)*100,2) dou1\n" +
            "from df_qms_ipqc_waig_total dqiwt \n" +
            "where dqiwt.f_seq ='丝印三层'\n" +
            "and ${ew.sqlSegment} " +
            "union all\n" +
            "select \n" +
            "'丝印线框' as str1\n" +
            ",format((sum(dqiwt.spot_check_count)-sum(dqiwt.affect_count))/sum(dqiwt.spot_check_count)*100,2) dou1\n" +
            "from df_qms_ipqc_waig_total dqiwt \n" +
            "where dqiwt.f_seq ='丝印线框'\n" +
            "and ${ew.sqlSegment} " +
            "union all\n" +
            "select \n" +
            "'丝印孔' as str1\n" +
            ",format((sum(dqiwt.spot_check_count)-sum(dqiwt.affect_count))/sum(dqiwt.spot_check_count)*100,2) dou1\n" +
            "from df_qms_ipqc_waig_total dqiwt \n" +
            "where dqiwt.f_seq ='丝印孔'\n" +
            "and ${ew.sqlSegment} " +
            "union all\n" +
            "select \n" +
            "'丝印四层' as str1\n" +
            ",format((sum(dqiwt.spot_check_count)-sum(dqiwt.affect_count))/sum(dqiwt.spot_check_count)*100,2) dou1\n" +
            "from df_qms_ipqc_waig_total dqiwt \n" +
            "where dqiwt.f_seq ='丝印四层'\n" +
            "and ${ew.sqlSegment} " +
            "union all\n" +
            "select \n" +
            "'丝印五层' as str1\n" +
            ",format((sum(dqiwt.spot_check_count)-sum(dqiwt.affect_count))/sum(dqiwt.spot_check_count)*100,2) dou1\n" +
            "from df_qms_ipqc_waig_total dqiwt \n" +
            "where dqiwt.f_seq ='丝印五层'\n" +
            "and ${ew.sqlSegment} " +
            "union all\n" +
            "select \n" +
            "'丝印六层' as str1\n" +
            ",format((sum(dqiwt.spot_check_count)-sum(dqiwt.affect_count))/sum(dqiwt.spot_check_count)*100,2) dou1\n" +
            "from df_qms_ipqc_waig_total dqiwt \n" +
            "where dqiwt.f_seq ='丝印六层'\n" +
            "and ${ew.sqlSegment} " +
            "union all\n" +
            "select \n" +
            "'孔移印' as str1\n" +
            ",format((sum(dqiwt.spot_check_count)-sum(dqiwt.affect_count))/sum(dqiwt.spot_check_count)*100,2) dou1\n" +
            "from df_qms_ipqc_waig_total dqiwt \n" +
            "where dqiwt.f_seq ='孔移印'\n" +
            "and ${ew.sqlSegment} " +
            "union all\n" +
            "select \n" +
            "'FQC1' as str1\n" +
            ",format((sum(dqiwt.spot_check_count)-sum(dqiwt.affect_count))/sum(dqiwt.spot_check_count)*100,2) dou1\n" +
            "from df_qms_ipqc_waig_total dqiwt \n" +
            "where dqiwt.f_seq ='FQC1'\n" +
            "and ${ew.sqlSegment} " +
            "union all\n" +
            "select \n" +
            "'BC-linker' as str1\n" +
            ",format(sum(if(item.`result`='OK',1,0))/count(0)*100,2) dou1\n" +
            "from df_bc_linker item \n" +
            "${ew_2.customSqlSegment} " +
            "union all\n" +
            "select \n" +
            "'AS' as str1\n" +
            ",format((sum(dqiwt.spot_check_count)-sum(dqiwt.affect_count))/sum(dqiwt.spot_check_count)*100,2) dou1\n" +
            "from df_qms_ipqc_waig_total dqiwt \n" +
            "where dqiwt.f_seq ='AS'\n" +
            "and ${ew.sqlSegment} " +
            "union all\n" +
            "select \n" +
            "'EMD' as str1\n" +
            ",format(sum(if(item.work_result='合格',1,0))/count(0)*100,2) dou1\n" +
            "from df_emd_detail item \n" +
            "${ew_2.customSqlSegment} " +
            "union all\n" +
            "select \n" +
            "'TZ' as str1\n" +
            ",format(sum(if(item.`result`='OK',1,0))/count(0)*100,2) dou1\n" +
            "from df_tz_detail item\n" +
            "${ew_2.customSqlSegment} " +
            "union all\n" +
            "select " +
            "'FQC2' as str1 \n" +
            ",format((sum(dqiwt.spot_check_count)-sum(dqiwt.affect_count))/sum(dqiwt.spot_check_count)*100,2) dou1\n" +
            "from df_qms_ipqc_waig_total dqiwt \n" +
            "where dqiwt.f_seq ='FQC2' " +
            "and ${ew.sqlSegment} ")
    List<Rate3> getFpy(@Param(Constants.WRAPPER)Wrapper<Rate3> wrapper,@Param("ew_2")Wrapper<Rate3> wrapper2);


    @Select("select \n" +
            "'ISM' as str1\n" +
            ",format(sum(if(item.`result`='OK',1,0))/count(0)*100,2) dou1\n" +
            "from df_ism_check_item_infos item \n" +
            "${ew_2.customSqlSegment} " +
            "union all\n" +
            "select \n" +
            "'FQC1' as str1\n" +
            ",format((sum(dqiwt.spot_check_count)-sum(dqiwt.affect_count))/sum(dqiwt.spot_check_count)*100,2) dou1\n" +
            "from df_qms_ipqc_waig_total dqiwt \n" +
            "where dqiwt.f_seq ='FQC1'\n" +
            "and ${ew.sqlSegment} " +
            "union all\n" +
            "select \n" +
            "'EMD' as str1\n" +
            ",format(sum(if(item.work_result='合格',1,0))/count(0)*100,2) dou1\n" +
            "from df_emd_detail item \n" +
            "${ew_2.customSqlSegment} " +
            "union all\n" +
            "select \n" +
            "'TZ' as str1\n" +
            ",format(sum(if(item.`result`='OK',1,0))/count(0)*100,2) dou1\n" +
            "from df_tz_detail item\n" +
            "${ew_2.customSqlSegment} " +
            "union all\n" +
            "select " +
            "'FQC2' as str1 \n" +
            ",format((sum(dqiwt.spot_check_count)-sum(dqiwt.affect_count))/sum(dqiwt.spot_check_count)*100,2) dou1\n" +
            "from df_qms_ipqc_waig_total dqiwt \n" +
            "where dqiwt.f_seq ='FQC2' " +
            "and ${ew.sqlSegment} ")
    List<Rate3> getAfterFpy(@Param(Constants.WRAPPER)Wrapper<Rate3> wrapper,@Param("ew_2")Wrapper<Rate3> wrapper2);

    @Select("select f_seq str1,f_bigpro str2,f_big_area str3,f_sort str4,rate dou1\n" +
            "FROM(\n" +
            "\tselect F_SEQ,F_BIGPRO,F_BIG_AREA,F_SORT,sum(if(DF_QMS_IPQC_WAIG_DETAIL.F_RESULT = 'ok',1,0)) ok,count(*) total,\n" +
            "       sum(if(DF_QMS_IPQC_WAIG_DETAIL.F_RESULT = 'ok',1,0)) / count(*) rate\n" +
            "\tFROM DF_QMS_IPQC_WAIG_TOTAL\n" +
            "\tJOIN DF_QMS_IPQC_WAIG_DETAIL ON DF_QMS_IPQC_WAIG_TOTAL.ID = DF_QMS_IPQC_WAIG_DETAIL.F_PARENT_ID\n" +
            "\twhere DF_QMS_IPQC_WAIG_DETAIL.F_TIME BETWEEN date_add(now(),INTERVAL -1 HOUR) AND now()\n" +
            "\tgroup by DF_QMS_IPQC_WAIG_TOTAL.F_SEQ,DF_QMS_IPQC_WAIG_TOTAL.F_BIGPRO,DF_QMS_IPQC_WAIG_DETAIL.F_BIG_AREA,DF_QMS_IPQC_WAIG_DETAIL.F_SORT\n" +
            ") t\n" +
            "where rate < #{alarmValue}")
	List<Rate4> getAlarmMessage(@Param("alarmValue") Double alarmValue);

    @Select("-- 外观单项不良预警\n" +
            "select f_seq str1,f_bigpro str2,f_big_area str3,f_sort str4,rate dou1\n" +
            "FROM(\n" +
            "\tselect F_SEQ,F_BIGPRO,F_BIG_AREA,F_SORT,sum(if(DF_QMS_IPQC_WAIG_DETAIL.F_RESULT = 'ok',1,0)) ok,count(*) total,\n" +
            "       sum(if(DF_QMS_IPQC_WAIG_DETAIL.F_RESULT = 'ok',1,0)) / count(*) rate\n" +
            "\tFROM DF_QMS_IPQC_WAIG_TOTAL\n" +
            "\tJOIN DF_QMS_IPQC_WAIG_DETAIL ON DF_QMS_IPQC_WAIG_TOTAL.ID = DF_QMS_IPQC_WAIG_DETAIL.F_PARENT_ID\n" +
            "\twhere DF_QMS_IPQC_WAIG_DETAIL.F_TIME BETWEEN date_add(now(),INTERVAL -1 HOUR) AND now()\n" +
            "\tgroup by DF_QMS_IPQC_WAIG_TOTAL.F_SEQ,DF_QMS_IPQC_WAIG_TOTAL.F_BIGPRO,DF_QMS_IPQC_WAIG_DETAIL.F_BIG_AREA,DF_QMS_IPQC_WAIG_DETAIL.F_SORT\n" +
            ") t\n" +
            "where rate > #{alarmValue} AND rate < #{warnValue}")
    List<Rate4> getWarnMessage(Double alarmValue, Double warnValue);


    @Select("SELECT DISTINCT\n" +
            "f_sort ,\n" +
            "detail.f_big_area ,\n" +
            "detail.f_sm_area  ,\n" +
            "count(*) AS count \n" +
            "FROM\n" +
            "df_qms_ipqc_waig_total total\n" +
            "left join df_qms_ipqc_waig_detail detail ON detail.f_parent_id = total.id \n" +
            "WHERE total.f_seq IN ( 'CNC1', 'CNC2', 'CNC3' )\n" +
            "AND detail.f_big_area is not null and detail.f_sort is not null and total.f_bigpro=#{project} \n" +
            "GROUP BY f_sort,detail.f_big_area,detail.f_sm_area")
    List<DfQmsIpqcWaigDetail> listSmallAreaInfo(@Param("project") String project);

    @Select("SELECT DISTINCT\n" +
            "f_sort ,\n" +
            "detail.f_big_area ,\n" +
            "detail.f_sm_area  ,\n" +
            "total.f_seq ,\n" +
            "count(*) AS count \n" +
            "FROM\n" +
            "df_qms_ipqc_waig_total total\n" +
            "left join df_qms_ipqc_waig_detail detail ON detail.f_parent_id = total.id \n" +
            "WHERE total.f_seq IN ( 'CNC1', 'CNC2', 'CNC3' )\n" +
            "AND detail.f_big_area is not null and detail.f_sort is not null and total.f_bigpro=#{project} \n" +
            "GROUP BY f_sort,detail.f_big_area,detail.f_sm_area,total.f_seq")
    List<DfQmsIpqcWaigDetail> listProcessInfo(@Param("project") String project);

}
