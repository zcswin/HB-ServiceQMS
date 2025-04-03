package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * TZ测量 Mapper 接口
 * </p>
 *
 * @author guangyao
 * @since 2023-09-11
 */
public interface DfTzDetailMapper extends BaseMapper<DfTzDetail> {

    @Select("select " +
            "sum(if(hour(check_time) between 5 and 6, 1, 0)) inte1, " +
            "sum(if(hour(check_time) between 7 and 8, 1, 0)) inte2, " +
            "sum(if(hour(check_time) between 9 and 10, 1, 0)) inte3, " +
            "sum(if(hour(check_time) between 11 and 12, 1, 0)) inte4, " +
            "sum(if(hour(check_time) between 13 and 14, 1, 0)) inte5, " +
            "sum(if(hour(check_time) between 15 and 16, 1, 0)) inte6, " +
            "sum(if(hour(check_time) between 17 and 18, 1, 0)) inte7, " +
            "sum(if(hour(check_time) between 19 and 20, 1, 0)) inte8, " +
            "sum(if(hour(check_time) between 21 and 22, 1, 0)) inte9, " +
            "sum(if(hour(check_time) = 23 or hour(check_time) = 0 , 1, 0)) inte10, " +
            "sum(if(hour(check_time) between 1 and 2, 1, 0)) inte11, " +
            "sum(if(hour(check_time) between 3 and 4, 1, 0)) inte12 " +
            "from df_tz_detail " +
            "${ew.customSqlSegment} ")
    Rate4 listTzNum(@Param(Constants.WRAPPER) Wrapper<DfTzDetail> wrapper);

    @Select("SELECT  " +
            "DATE_FORMAT(DATE_SUB(check_time, INTERVAL 7 HOUR),'%m-%d') str1, " +
            "sum(if(Hour(DATE_SUB(check_time, INTERVAL 7 HOUR)) between 0 and 11, 1, 0)) inte1, " +
            "sum(if(Hour(DATE_SUB(check_time, INTERVAL 7 HOUR)) between 12 and 23, 1, 0)) inte2 " +
            "FROM df_tz_detail " +
            "${ew.customSqlSegment} " +
            "group by str1 " +
            "order by str1")
    List<Rate3> listAllNumGroupByDate(@Param(Constants.WRAPPER) Wrapper<DfTzDetail> wrapper);




    @Select("select machine_code str1, " +
            "FORMAT(sum(if(check_type = 1 and result = 'OK', 1, 0))/count(0)*100,2) dou1," +
            "FORMAT(sum(if(result = 'OK', 1, 0))/count(0)*100,2) dou2 " +
            "from df_tz_detail " +
            "${ew.customSqlSegment} " +
            "group by str1")
    List<Rate3> listMachineOneAndMutilOkRate(@Param(Constants.WRAPPER) Wrapper<DfTzDetail> wrapper);

    @Select("select DATE_FORMAT(DATE_SUB(check_time, INTERVAL 7 HOUR),'%c/%e') str1, " +
            "FORMAT(sum(if(check_type = 1 and result = 'OK', 1, 0))/count(0)*100,2) dou1," +
            "FORMAT(sum(if(result = 'OK', 1, 0))/count(0)*100,2) dou2 " +
            "from df_tz_detail " +
            "${ew.customSqlSegment} " +
            "group by str1")
    List<Rate3> listDateOneAndMutilOkRate(@Param(Constants.WRAPPER) Wrapper<DfTzDetail> wrapper);

    @Select("select  " +
            "FORMAT(sum(if(check_type = 1 and result = 'OK', 1, 0)) / sum(if(check_type = 1, 1, 0))*100,2) dou1, " +
            "FORMAT(sum(if(check_type = 2 and result = 'OK', 1, 0)) / sum(if(check_type = 2, 1, 0))*100,2) dou2 " +
            "from df_tz_detail " +
            "${ew.customSqlSegment} ")
    List<Rate3> listOKRate(@Param(Constants.WRAPPER) Wrapper<DfTzDetail> wrapper);

    @Select("SELECT  " +
            "DATE_FORMAT(DATE_SUB(check_time, INTERVAL 7 HOUR),'%m-%d') str1, " +
            "FORMAT(sum(if(Hour(DATE_SUB(check_time, INTERVAL 7 HOUR)) between 0 and 11 and result = 'OK', 1, 0)) / sum(if(Hour(DATE_SUB(check_time, INTERVAL 7 HOUR)) between 0 and 11, 1, 0))*100,2) dou1, " +
            "FORMAT(sum(if(Hour(DATE_SUB(check_time, INTERVAL 7 HOUR)) between 12 and 23 and result = 'OK', 1, 0)) / sum(if(Hour(DATE_SUB(check_time, INTERVAL 7 HOUR)) between 12 and 23, 1, 0))*100,2) dou2 " +
            "FROM df_tz_detail " +
            "${ew.customSqlSegment} " +
            "group by str1 " +
            "order by str1")
    List<Rate3> listOkRateGroupByDate(@Param(Constants.WRAPPER) Wrapper<DfTzDetail> wrapper);


//    工厂不良TOP10分布对比2(按每个缺陷的点进行分类)
//    @Select("select item.item_name str1, item.check_type str2 " +
//            ",sum(if(item.check_result = 'NG',1, 0)) inte1 " +
//            ",FORMAT(sum(if(item.check_result = 'NG',1, 0))/ count(*)*100,2) dou1 " +
//            "from df_tz_check_item_infos item " +
//            "left join df_tz_detail dtd on item.check_id = dtd.id " +
//            "${ew.customSqlSegment} " +
//            "group by str1, str2 " +
//            "order by dou1 desc " +
//            "limit 10")
//    List<Rate3> listItemNgRateTop(@Param(Constants.WRAPPER) Wrapper<DfTzDetail> wrapper);

//  工厂不良TOP10分布对比2(按每个缺陷进行分类)
    @Select("with item_new as(\n" +
            "\tselect item.check_id id,${selectNameString}\n" +
            "\t,sum(if(item.check_result='NG',1,0)) ngNumber\n" +
            "\t,if(sum(if(item.check_result='NG',1,0))>0,'NG','OK') checkResult \n" +
            "\t,count(0) total\n" +
            "\tfrom df_tz_check_item_infos item \n" +
            "\tleft join df_tz_detail dtd on item.check_id = dtd.id\n" +
            "${ew.customSqlSegment} " +
            "\tgroup by item.check_id,item.item_name\n" +
            ")\n" +
            "select \n" +
            "name str1\n" +
            ",sum(if(checkResult='NG',1,0)) inte1\n" +
            ",count(0) inte2\n" +
            ",FORMAT(sum(if(checkResult='NG',1,0))/ count(0)*100,2) dou1\n" +
            "from item_new\n" +
            "group by name\n" +
            "order by inte1 desc " +
            "limit 10")
    List<Rate3> listItemNgRateTop(@Param(Constants.WRAPPER) Wrapper<DfTzDetail> wrapper,String selectNameString);

    @Select("select  " +
            "count(*) inte1, " +
            "FORMAT(sum(if(result = 'OK', 1, 0)) / count(*)*100,2) dou1 " +
            "from df_lead_detail " +
            "${ew.customSqlSegment} ")
    List<Rate3> listAllOkRateTop(@Param(Constants.WRAPPER) Wrapper<DfTzDetail> wrapper);



    @Select("select  machine_code str1, pos str2," +
            "FORMAT(sum(if(result = 'OK', 1, 0)) / count(*)*100,2) dou1 " +
            "from df_tz_detail " +
            "${ew.customSqlSegment} " +
            "group by machine_code,pos " +
            "order by machine_code,pos asc")
    List<Rate3> listWorkPositionOKRate(@Param(Constants.WRAPPER) Wrapper<DfTzDetail> wrapper);



    @Select("select item.* " +
            "from df_tz_check_item_infos item " +
            "left join df_tz_detail dtd on item.check_id = dtd.id " +
            "${ew.customSqlSegment} ")
    List<DfTzCheckItemInfos> listItemInfosJoinDetail(@Param(Constants.WRAPPER) Wrapper<DfTzDetail> wrapper);

    // TZ不良走势
//    @Select("select \n" +
//            "date_format(date_sub(check_time, interval 7 hour),'%Y-%m-%d') str1\n" +
//            ",sum(if(`result` = 'NG',1,0)) inte1\n" +
//            ",count(0) inte2\n" +
//            ",round(sum(if(`result` = 'NG',1,0))/count(0) * 100,2) dou1\n" +
//            "from df_tz_detail\n" +
//            " ${ew.customSqlSegment} " +
//            "group by str1\n" +
//            "order by str1 asc")
//    List<Rate3> getNgRate(@Param(Constants.WRAPPER) Wrapper<DfTzDetail> wrapper);

//    @Select("select \n" +
//            "date_format(check_time,'%Y-%m-%d') str1\n" +
//            ",sum(if(`result` = 'NG',1,0)) inte1\n" +
//            ",count(0) inte2\n" +
//            ",round(sum(if(`result` = 'NG',1,0))/count(0) * 100,2) dou1\n" +
//            "from df_tz_detail\n" +
//            " ${ew.customSqlSegment} " +
//            "group by str1\n" +
//            "order by str1 asc")
//    List<Rate3> getNgRate(@Param(Constants.WRAPPER) Wrapper<DfTzDetail> wrapper);
//
//    // TZ不良Top10
//    @Select("with defect_name as(\n" +
//            "\tselect ipqc_name ,name\n" +
//            "\tfrom df_size_cont_relation\n" +
//            " where `type` = 'TZ' and process = 'TZ'" +
//            ")\n" +
//            "select \n" +
//            "dn.ipqc_name str1\n" +
//            ",sum(if(dtcii.check_result = 'NG',1,0)) inte1\n" +
//            ",count(0) inte2\n" +
//            ",round(sum(if(dtcii.check_result = 'NG',1,0))/count(0) * 100,4) dou1 \n" +
//            "from df_tz_detail dtd \n" +
//            "inner join df_tz_check_item_infos dtcii \n" +
//            "on dtcii.check_id = dtd.id \n" +
//            "inner join defect_name dn \n" +
//            "on dn.name = dtcii.check_name \n" +
//            " ${ew.customSqlSegment} " +
//            "group by dn.ipqc_name\n" +
//            "having dou1 > 0\n" +
//            "order by dou1 desc")
//    List<Rate3> getNgDetailRateTop10(@Param(Constants.WRAPPER) Wrapper<DfTzDetail> wrapper);

}
