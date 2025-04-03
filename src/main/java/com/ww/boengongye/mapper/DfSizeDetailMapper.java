package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 尺寸数据 Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2023-02-28
 */
public interface DfSizeDetailMapper extends BaseMapper<DfSizeDetail> {

    @Select("select machine_code, " +
            "sum(if(result='OK', 1, 0)) / count(machine_code) as appear_length1 " +
            "from df_size_detail " +
            "${ew.customSqlSegment} " +
            "group by machine_code " +
            "order by appear_length1 desc " +
            "limit 10")
    List<DfSizeDetail> listAllByFactory(@Param(Constants.WRAPPER) Wrapper<DfSizeNgRate> wrapper);

    @Select("select " +
            "format(sum(if(result='OK', 1, 0)) / count(*),4) as project " +
            "from df_size_detail " +
            "${ew.customSqlSegment} ")
    DfSizeDetail getOkRate(@Param(Constants.WRAPPER) Wrapper<DfSizeNgRate> wrapper);

    @Select("SELECT r.*, fac.factory_name FROM df_size_detail r " +
            "left join df_factory fac on r.factory = fac.factory_code " +
            "${ew.customSqlSegment}")
    IPage<DfSizeDetail> listJoinIds(IPage<DfSizeDetail> page, @Param(Constants.WRAPPER) Wrapper<DfSizeDetail> wrapper);

    @Select("select machine_code, factory, project, process, linebody, day_or_night " +
            "from df_size_detail " +
            "${ew.customSqlSegment} " +
            "group by machine_code,factory, project, process, linebody, day_or_night ")
    List<DfSizeDetail> listMachineCode(@Param(Constants.WRAPPER) Wrapper<DfSizeDetail> wrapper);

    @Select("select factory, sum(if(result='OK',1,0)) id " +
            "from df_size_detail " +
            "where (machine_code, factory, create_time) in " +
            "(select machine_code, factory, min(create_time) create_time " +
            "from df_size_detail " +
            "${ew.customSqlSegment} " +
            "group by machine_code, factory) " +
            "group by factory")
    List<DfSizeDetail> getFaiPassRate(@Param(Constants.WRAPPER) Wrapper<DfSizeDetail> wrapper);

    @Select("select date_format(DATE_SUB(create_time, INTERVAL 7 HOUR), '%m-%d') date, sum(if(result='OK', 1, 0)) / count(*) rate " +
            "from df_size_detail " +
            "${ew.customSqlSegment} " +
            "group by date " +
            "order by date ")
    List<Rate> listDateOkRate(@Param(Constants.WRAPPER) Wrapper<DfSizeDetail> wrapper);

    @Select("select date_format(date, '%m-%d') date,rate from( select date_format(create_time, '%y-%m-%d') date, sum(ok_num) / sum(all_num) rate  " +
            "from df_size_ok_rate " +
            "${ew.customSqlSegment} " +
            "group by date " +
            "order by date)t")  // 通过新表获取良率
    List<Rate> listDateOkRate2(@Param(Constants.WRAPPER) Wrapper<DfSizeDetail> wrapper);

    @Select("select sum(if(result = 'OK', 1, 0)) / count(*) rate " +
            "from df_size_detail " +
            "${ew.customSqlSegment} ")
    Rate getSizeRealRate(@Param(Constants.WRAPPER) Wrapper<DfSizeDetail> wrapper);

    /*@Select(" select pro_res_rate.*, @total := @total * ok_rate as rate from  " +
            "(select process_name name, sort, if(ok_rate is null, 1, ok_rate) ok_rate from      " +
            " (select process_name, sort     " +
            " from df_process     " +
            " where sort is not null and process_name != 'SSB' ) pro     " +
            " left join      " +
            " (select process, sum(if(result!='NG',1,0))/count(*)  ok_rate " +
            "from df_size_detail " +
            "${ew.customSqlSegment} " +
            "group by process) pro_res_ok_rate on pro.process_name = pro_res_ok_rate.process     " +
            " order by sort) pro_res_rate, (select @total := 1) as temp  ")*/
    @Select("  select pro_res_rate.*, @total := @total * ok_rate as rate from      " +
            "             (select process_name name, sort, ok_rate from          " +
            "              (select process_name, sort         " +
            "              from df_process_project_config         " +
            "              where sort is not null ) pro         " +
            "              left join          " +
            "              (select process, sum(if(result!='NG',1,0))/count(*)  ok_rate     " +
            "             from df_size_detail     " +
            "             ${ew.customSqlSegment} " +
            "             group by process) pro_res_ok_rate on pro.process_name = pro_res_ok_rate.process  " +
            "             where ok_rate is not null " +
            "              order by sort) pro_res_rate, (select @total := 1) as temp   ")
    List<Rate> listAlwaysOkRate(@Param(Constants.WRAPPER) Wrapper<DfSizeDetail> wrapper);

    @Select("select pro_res_rate.*, @total := @total * ok_rate as rate from        " +
            "(select process_name name, sort, ok_rate from            " +
            " (select process_name, sort           " +
            " from df_process_project_config           " +
            " where sort is not null and project like #{project} ) pro           " +
            " left join            " +
            " (select process, sum(ok_num) / sum(all_num)  ok_rate       " +
            "from df_size_ok_rate       " +
            "${ew.customSqlSegment} and project like #{project} " +
            "group by process) pro_res_ok_rate on pro.process_name = pro_res_ok_rate.process    " +
            "where ok_rate is not null   " +
            " order by sort) pro_res_rate, (select @total := 1) as temp   ") // 通过新表查询直通良率
    List<Rate> listAlwaysOkRate2(@Param(Constants.WRAPPER) Wrapper<DfSizeDetail> wrapper, String project);

    @Select("select pro_ng_num.process name, pro_ng_num.ng_num ng_num, pro_all_num.all_num all_num, pro_ng_num.ng_num/pro_all_num.all_num rate from " +
            "(select det.process, sum(if(item.check_result='NG',1,0)) ng_num " +
            "from df_size_check_item_infos item " +
            "left join df_size_detail det on item.check_id = det.id " +
            "where det.test_time between #{startTime} and #{endTime} and HOUR(DATE_SUB(det.test_time, INTERVAL 7 HOUR)) between #{startHour} and #{endHour} and item.item_name = #{ngItem} and item.key_point = 1  " +
            "group by det.process) pro_ng_num " +
            "left join  " +
            "(select process, count(*) all_num " +
            "from df_size_detail " +
            "where test_time between #{startTime} and #{endTime} and HOUR(DATE_SUB(test_time, INTERVAL 7 HOUR)) between #{startHour} and #{endHour} " +
            "group by process) pro_all_num on pro_ng_num.process = pro_all_num.process order by rate desc")
    List<Rate> listProcessNgRateByNgItem(String ngItem, String startTime, String endTime, Integer startHour, Integer endHour);

    @Select("select process name, sum(ng_num) ng_num, sum(all_num) all_num, sum(ng_num) / sum(all_num) rate " +
            "from df_size_item_ng_rate " +
            "${ew.customSqlSegment} " +
            "group by process " +
            "order by rate desc") // 通过新表获取
    List<Rate> listProcessNgRateByNgItem2(@Param(Constants.WRAPPER) Wrapper<DfSizeDetail> wrapper);

    @Select("select pro_ng_num.machine_code name, pro_ng_num.ng_num ng_num, pro_all_num.all_num all_num, pro_ng_num.ng_num/pro_all_num.all_num rate from " +
            "(select det.machine_code, sum(if(item.check_result='NG',1,0)) ng_num " +
            "from df_size_check_item_infos item " +
            "left join df_size_detail det on item.check_id = det.id " +
            "where det.test_time between #{startTime} and #{endTime} and det.project like #{project} and det.item_name like #{color} and HOUR(DATE_SUB(det.test_time, INTERVAL 7 HOUR)) between #{startHour} and #{endHour} and item.item_name = #{ngItem} and det.process like #{process} and item.key_point = 1 " +
            "group by det.machine_code) pro_ng_num " +
            "left join " +
            "(select * from df_process where process_name like #{process}) pro on pro_ng_num.machine_code like concat(pro.first_code, '%') " +
            "left join  " +
            "(select machine_code, count(*) all_num " +
            "from df_size_detail " +
            "where test_time between #{startTime} and #{endTime} and project like #{project} and item_name like #{color} and process like #{process} and HOUR(DATE_SUB(test_time, INTERVAL 7 HOUR)) between #{startHour} and #{endHour} " +
            "group by machine_code) pro_all_num on pro_ng_num.machine_code = pro_all_num.machine_code " +
            "where pro.first_code is not null " +
            "order by rate desc, pro_ng_num.machine_code")
    List<Rate> listMacNgRateByNgItemAndProcess(String process,String project,String color, String ngItem, String startTime, String endTime, Integer startHour, Integer endHour);

    @Select("select process name, sum(if(result='OK',1,0)) / count(*) rate, pro.sort from df_size_detail det " +
            "left join (SELECT * FROM `df_process_project_config` where type like '%尺寸%') pro on pro.process_name = det.process  " +
            "${ew.customSqlSegment} " +
            "group by det.process, pro.sort " +
            "order by pro.sort")
    List<Rate> listAllProcessOkRate(@Param(Constants.WRAPPER) Wrapper<DfSizeDetail> wrapper);

    @Select("select det.process name, sum(ok_num) / sum(all_num) rate,dyw.prewarning_value passTarget, pro.sort from df_size_ok_rate det   " +
            "            left join (SELECT * FROM `df_process_project_config` where type like '%尺寸%' and project like #{project}) pro on pro.process_name = det.process " +
            "            left join df_yield_warn dyw on dyw.process = det.process and dyw.name = '尺寸良率' " +
            "            ${ew.customSqlSegment} " +
            "            group by det.process,dyw.prewarning_value,pro.sort   " +
            "            order by pro.sort")
    List<Rate> listAllProcessOkRate2(@Param(Constants.WRAPPER) Wrapper<DfSizeDetail> wrapper,String project);

    @Select("select t1.date, t1.day_or_night1 day_or_night, t2.rate from ( " +
            "select * from ( " +
            "select DATE_FORMAT(DATE_SUB(test_time,INTERVAL 7 HOUR),'%m-%d') date from df_size_detail det " +
            "${ew.customSqlSegment}) t, " +
            "(select 'A' day_or_night1 union select 'B' day_or_night1) day_or_night1 " +
            "group by date, day_or_night1) t1 " +
            "left join " +
            "(select DATE_FORMAT(DATE_SUB(test_time,INTERVAL 7 HOUR),'%m-%d') date, if(hour(DATE_SUB(test_time,INTERVAL 7 HOUR)) > 11, 'B', 'A') day_or_night1, sum(if(result='NG',1,0)) / count(*) rate from df_size_detail " +
            "${ew.customSqlSegment} " +
            "GROUP BY date, day_or_night1) t2 on t1.date = t2.date and t1.day_or_night1 = t2.day_or_night1 " +
            "ORDER BY date")
    List<Rate> listDateNgRate(@Param(Constants.WRAPPER) Wrapper<DfSizeDetail> wrapper);

    @Select("select item.item_name name, sum(if(item.check_result='NG',1,0)) / count(*) rate from df_size_detail det " +
            "left join df_size_check_item_infos item on det.id = item.check_id " +
            "${ew.customSqlSegment} " +
            "group by item.item_name " +
            "order by rate desc")
    List<Rate> listItemNgRate(@Param(Constants.WRAPPER) Wrapper<DfSizeDetail> wrapper);

    @Select("select t.date str1, t.open_num inte1, t.open_num / t.all_num dou1, t3.first_check_open_num / t.open_num dou2, t4.first_check_ok_num / t.open_num dou3 from " +
            "(select * from  " +
            "(select count(distinct det.machine_code) open_num, DATE_FORMAT(DATE_SUB(det.test_time, INTERVAL 7 HOUR),'%m-%d') date from df_size_detail det " +
            "left join df_process pro on det.machine_code like concat(pro.first_code, '%') " +
            "${ew.customSqlSegment} " +
            "group by date) t1, " +
            "(select count(*) all_num from df_mac_status_size size " +
            "left join df_process pro on size.MachineCode like concat(pro.first_code, '%') " +
            "where pro.process_name like #{process}) t2) t " +
            "left join  " +
            " " +
            "(select count(distinct det.machine_code) first_check_open_num, DATE_FORMAT(DATE_SUB(det.test_time, INTERVAL 7 HOUR),'%m-%d') date from df_size_detail det " +
            "left join df_process pro on det.machine_code like concat(pro.first_code, '%') " +
            "${ew.customSqlSegment} and det.check_type = 3 " +
            "group by date) t3 on t.date = t3.date " +
            " " +
            "left join  " +
            "(select count(distinct det.machine_code) first_check_ok_num, DATE_FORMAT(DATE_SUB(det.test_time, INTERVAL 7 HOUR),'%m-%d') date from df_size_detail det " +
            "left join df_process pro on det.machine_code like concat(pro.first_code, '%') " +
            "${ew.customSqlSegment} and det.check_type = 3 and det.result = 'OK' " +
            "group by date) t4 on t.date = t4.date")
    List<Rate3> listOpenRate(@Param(Constants.WRAPPER) Wrapper<DfSizeDetail> wrapper, String process);

    @Select("SELECT\n" +
            "\tdd.rate \n" +
            "FROM\n" +
            "\t(\n" +
            "SELECT\n" +
            "\tpro_res_rate.*,\n" +
            "\t@total := @total * ok_rate AS rate \n" +
            "FROM\n" +
            "\t(\n" +
            "SELECT\n" +
            "\tprocess_name NAME,\n" +
            "\tsort,\n" +
            "IF\n" +
            "\t( ok_rate IS NULL OR ok_rate = 0, 1, ok_rate ) ok_rate \n" +
            "FROM\n" +
            "\t( SELECT process_name, sort FROM df_process_project_config WHERE sort IS NOT NULL ) pro\n" +
            "\tLEFT JOIN (\n" +
            "SELECT\n" +
            "\tprocess,\n" +
            "\tsum( IF ( result != 'NG', 1, 0 ) ) / count( * ) ok_rate \n" +
            "FROM\n" +
            "\tdf_size_detail \n" +
            " ${ew.customSqlSegment} \n" +
            "GROUP BY\n" +
            "\tprocess \n" +
            "\t) pro_res_ok_rate ON pro.process_name = pro_res_ok_rate.process \n" +
            "WHERE\n" +
            "\tok_rate IS NOT NULL \n" +
            "ORDER BY\n" +
            "\tsort \n" +
            "\t) pro_res_rate,\n" +
            "\t( SELECT @total := 1 ) AS temp \n" +
            "\t) dd \n" +
            "ORDER BY\n" +
            "\trate ASC \n" +
            "\tLIMIT 1")
          Rate getAllProcessYield(@Param(Constants.WRAPPER) Wrapper<DfSizeDetail> wrapper);


    @Select("SELECT\n" +
            "sum(if(result='OK',1,0)) /count(*) * 100 as  rate\n" +
            "FROM\n" +
            "\t`df_size_detail` sd\n" +
            "\tJOIN df_machine mac ON sd.machine_code = mac.code ${ew.customSqlSegment} ")
    Rate getLineYield(@Param(Constants.WRAPPER) Wrapper<DfSizeDetail> wrapper);

    @Select("with temp_date_machine as (\n" +
            "\tselect DATE_FORMAT(DATE_SUB(test_time,INTERVAL 7 HOUR),'%m-%d') date,machine_code,sum(if(result='NG',1,0))/COUNT(*) rate\n" +
            "\tFROM df_size_detail detail\n" +
            "\twhere test_time between date_sub(now(),INTERVAL 7 DAY ) and now()\n" +
            "\t    and weekday(DATE_SUB(test_time,INTERVAL 7 HOUR)) != 6\n" +
            "\tGROUP BY date,machine_code\n" +
            ")\n" +
            ",temp_rn as(\n" +
            "\tselect * ,row_number() OVER (PARTITION BY date ORDER BY rate DESC ) as no\n" +
            "\tfrom temp_date_machine\n" +
            ")\n" +
            "select MACHINE_CODE\n" +
            "from temp_rn\n" +
            "where no <= 3\n" +
            "GROUP BY MACHINE_CODE\n" +
            "HAVING count(*) >=6")
	List<DfSizeDetail> weekOfPoorTOP3Warning(@Param(Constants.WRAPPER) QueryWrapper<DfSizeDetail> ew);

    @Select("with size_defect as(\n" +
            "\tselect \n" +
            "\tdsd.process process\n" +
            "\t,dscii.item_name defectName\n" +
            "\t,date_format(date_sub(dscii.check_time,interval 7 hour),'%Y-%m-%d') checkTime\n" +
            "\t,sum(if(dscii.check_result='NG',1,0)) ngNumber \n" +
            "\tfrom df_size_check_item_infos dscii  \n" +
            "\tinner join df_size_detail dsd\n" +
            "\ton dsd.id = dscii.check_id\n" +
            "\twhere dscii.check_time between date_sub(now(),interval 14 day) and now()  \n" +
            "\tand weekday(date_sub(dscii.check_time,interval 7 hour)) !=6\n" +
            "\tgroup by dsd.process,dscii.item_name,date_format(date_sub(dscii.check_time,interval 7 hour),'%Y-%m-%d')  \n" +
            ")\n" +
            ",defect_top as(\n" +
            "\tselect *\n" +
            "\t,row_number()over(partition by checkTime order by ngNumber desc) num\n" +
            "\tfrom size_defect\n" +
            ")\n" +
            "select \n" +
            "process str1\n" +
            ",defectName str2\n" +
            ",count(0) inte1\n" +
            "from defect_top\n" +
            "where num<=3\n" +
            "group by process,defectName\n" +
            "having count(0)>=12")
    List<Rate3> twoWeekOfPoorTOP3Warning();


    @Select("select\n" +
            "dsd.id id\n" +
            ",dsd.work_time\n" +
            ",dsd.machine_code\n" +
            ",dsd.`result`\n" +
            ",dsd.factory \n" +
            ",dsd.process \n" +
            ",dsd.project \n" +
            ",dsd.item_name\n" +
            ",dsd.test_time\n" +
            ",dsd.sn \n" +
            ",dsd.fixture_id\n" +
            ",dsd.machine_life\n" +
            ",dsd.knife_first_life\n" +
            ",dsd.knife_second_life \n" +
            ",dsd.knife_third_life \n" +
            ",dsd.knife_fourth_life \n" +
            ",dsd.change_knife_status \n" +
            ",dsd.debug_status\n" +
            ",dsd.change_class_status\n" +
            ",dsd.change_clamp_status \n" +
            ",dscii.item_name defect_name\n" +
            ",dscii.check_value\n" +
            ",dscii.check_result\n" +
            "from df_size_detail dsd \n" +
            "left join df_size_check_item_infos dscii \n" +
            "on dscii.check_id = dsd.id \n" +
            " ${ew.customSqlSegment} ")
    List<Map<String,Object>> getSizeDetailInfoList(@Param(Constants.WRAPPER) QueryWrapper<Map<String,Object>> qw);


    @Select("select\n" +
            "dsd.machine_code str1\n" +
            ",sum(if(dsd.`result` = 'OK',1,0)) inte1\n" +
            ",count(0) inte2 \n" +
            "from df_size_detail dsd \n" +
            " ${ew.customSqlSegment} " +
            "group by machine_code ")
    List<Rate3> getMachineOkNumList(@Param(Constants.WRAPPER) QueryWrapper<Rate3> qw);

    @Select("select \n" +
            "dqiwd.f_product_id\n" +
            ",dqiwd.f_parent_id \n" +
            ",dqiwt.f_stage \n" +
            ",dqiwt.f_line \n" +
            ",dqiwt.status \n" +
            ",dqiwd.f_time \n" +
            ",dqiwd.f_result \n" +
            ",dqiwd.f_sort \n" +
            "from df_qms_ipqc_waig_detail dqiwd \n" +
            "left join df_qms_ipqc_waig_total dqiwt \n" +
            "on dqiwd.f_parent_id = dqiwt.id \n" +
            " ${ew.customSqlSegment} ")
    List<Map<String,Object>> getQmsWaigInfoList(@Param(Constants.WRAPPER) QueryWrapper<Map<String,Object>> qw);

    @Select("select \n" +
            "waig_new.str1\n" +
            ",sum(waig_new.inte2)-sum(waig_new.inte1) inte1\n" +
            ",sum(waig_new.inte2) inte2\n" +
            "from \n" +
            "\t(select \n" +
            "\tdqiwt.f_mac str1\n" +
            "\t,dqiwd.f_parent_id str2\n" +
            "\t,dqiwt.affect_count inte1\n" +
            "\t,dqiwt.spot_check_count inte2\n" +
            "\tfrom df_qms_ipqc_waig_detail dqiwd \n" +
            "\tleft join df_qms_ipqc_waig_total dqiwt \n" +
            "\ton dqiwd.f_parent_id = dqiwt.id \n" +
            " ${ew.customSqlSegment} " +
            "\tgroup by str1,str2,inte1,inte2\n" +
            "\t)waig_new\n" +
            "group by str1")
    List<Rate3> getQmsWaigMachineOkNumList(@Param(Constants.WRAPPER) QueryWrapper<Rate3> qw);

    @Select("select *,ROUND( 1 - (ng / total), 2)*100 as qa\n" +
            "from (\n" +
            "\tselect \n" +
            "\tdate_format(DATE_SUB(create_time, INTERVAL 7 HOUR), '%Y-%m-%d') date,\n" +
            "\tproject,\n" +
            "\t'尺寸' as type,\n" +
            "\tprocess,\n" +
            "\tcount(*) as total,\n" +
            "\tsum(if(info_result = 'NG',1,0)) as ng\n" +
            "\tfrom df_size_detail\n" +
            " ${ew.customSqlSegment} " +
            "\tORDER BY date DESC\n" +
            ") as t")
    List<Map<String, Object>> exportInspectionTableForProcess(@Param(Constants.WRAPPER) QueryWrapper<DfSizeDetail> sizeWrapper);

    @Select("select *,ROUND( 1 - (ng / total), 2)*100 as qa\n" +
            "from (\n" +
            "\tselect \n" +
            "\tdate_format(DATE_SUB(create_time, INTERVAL 7 HOUR), '%Y-%m-%d') date,\n" +
            "\tproject,\n" +
            "\t'尺寸' as type,\n" +
            "\tprocess,\n" +
            "\tmachine_code as machineCode,\n" +
            "\tcount(*) as total,\n" +
            "\tsum(if(info_result = 'NG',1,0)) as ng\n" +
            "\tfrom df_size_detail\n" +
            " ${ew.customSqlSegment} " +
            "\tORDER BY date DESC\n" +
            ") as t")
    List<Map<String, Object>> exportInspectionTableForProcessBymachineId(@Param(Constants.WRAPPER) QueryWrapper<DfSizeDetail> sizeWrapper);

    @Select("select *,ROUND( (ng / total), 2)*100 as qa\n" +
            "from (\n" +
            "\tselect \n" +
            "\tdate_format(DATE_SUB(df_size_detail.create_time, INTERVAL 7 HOUR), '%Y-%m-%d') date,\n" +
            "\tproject,\n" +
            "\t'尺寸' as type,\n" +
            "\tprocess,\n" +
            "\tdf_size_check_item_infos.item_name as item,\n" +
            "\tcount(*) as total,\n" +
            "\tsum(if(info_result = 'NG',1,0)) as ng\n" +
            "\tfrom df_size_detail\n" +
            "\tLEFT JOIN df_size_check_item_infos ON df_size_detail.id = df_size_check_item_infos.check_id\n" +
            " ${ew.customSqlSegment} " +
            "\tORDER BY date DESC\n" +
            ") as t")
    List<Map<String, Object>> exportNgClassificationScale(@Param(Constants.WRAPPER) QueryWrapper<DfSizeDetail> sizeWrapper);
}
