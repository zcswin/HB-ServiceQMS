package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2023-08-28
 */
public interface DfLeadDetailMapper extends BaseMapper<DfLeadDetail> {

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
            "from df_lead_detail " +
            "${ew.customSqlSegment} ")
    Rate4 listLeadNum(@Param(Constants.WRAPPER) Wrapper<DfLeadDetail> wrapper);

    @Select("select  " +
            "sum(if(check_type = 1 and result = 'OK', 1, 0)) / sum(if(check_type = 1, 1, 0)) dou1, " +
            "sum(if(result = 'OK', 1, 0)) / count(*) dou2 " +
            "from df_lead_detail " +
            "${ew.customSqlSegment} ")
    List<Rate3> listOKRate(@Param(Constants.WRAPPER) Wrapper<DfLeadDetail> wrapper);

    @Select("select  " +
            "count(*) inte1, " +
            "sum(if(result = 'OK', 1, 0)) / count(*) dou1 " +
            "from df_lead_detail " +
            "${ew.customSqlSegment} ")
    List<Rate3> listAllOkRateTop(@Param(Constants.WRAPPER) Wrapper<DfLeadDetail> wrapper);

    @Select("select  machine_code str1, work_position str2, " +
            "sum(if(result = 'OK', 1, 0)) / count(*) dou1 " +
            "from df_lead_detail " +
            "${ew.customSqlSegment} " +
            "group by machine_code, work_position " +
            "order by machine_code, work_position")
    List<Rate3> listWorkPositionOKRate(@Param(Constants.WRAPPER) Wrapper<DfLeadDetail> wrapper);

    @Select("SELECT  " +
            "DATE_FORMAT(DATE_SUB(check_time, INTERVAL 7 HOUR),'%m-%d') str1, " +
            "sum(if(Hour(DATE_SUB(check_time, INTERVAL 7 HOUR)) between 0 and 11 and result = 'OK', 1, 0)) / sum(if(Hour(DATE_SUB(check_time, INTERVAL 7 HOUR)) between 0 and 11, 1, 0)) dou1, " +
            "sum(if(Hour(DATE_SUB(check_time, INTERVAL 7 HOUR)) between 12 and 23 and result = 'OK', 1, 0)) / sum(if(Hour(DATE_SUB(check_time, INTERVAL 7 HOUR)) between 12 and 23, 1, 0)) dou2 " +
            "FROM `df_lead_detail` " +
            "${ew.customSqlSegment} " +
            "group by str1 " +
            "order by str1")
    List<Rate3> listOkRateGroupByDate(@Param(Constants.WRAPPER) Wrapper<DfLeadDetail> wrapper);

    @Select("SELECT  " +
            "DATE_FORMAT(DATE_SUB(check_time, INTERVAL 7 HOUR),'%m-%d') str1, " +
            "sum(if(Hour(DATE_SUB(check_time, INTERVAL 7 HOUR)) between 0 and 11, 1, 0)) inte1, " +
            "sum(if(Hour(DATE_SUB(check_time, INTERVAL 7 HOUR)) between 12 and 23, 1, 0)) inte2 " +
            "FROM `df_lead_detail` " +
            "${ew.customSqlSegment} " +
            "group by str1 " +
            "order by str1 ")
    List<Rate3> listAllNumGroupByDate(@Param(Constants.WRAPPER) Wrapper<DfLeadDetail> wrapper);

    @Select("select ${selectNameString}, item.check_type str2, sum(if(item.check_result = 'NG',1, 0))/ count(*) dou1 " +
            "from df_lead_check_item_infos item " +
            "left join df_lead_detail det on item.check_id = det.id " +
            "${ew.customSqlSegment} " +
            "group by str1, str2 " +
            "order by dou1 desc " +
            "limit 10")
    List<Rate3> listItemNgRateTop(@Param(Constants.WRAPPER) Wrapper<DfLeadDetail> wrapper,String selectNameString);

    @Select("select machine_code str1, " +
            "sum(if(check_type = 1 and result = 'OK', 1, 0)) / count(*) dou1, " +
            "sum(if(result = 'OK', 1, 0)) / count(*) dou2  " +
            "from df_lead_detail " +
            "${ew.customSqlSegment} " +
            "group by str1")
    List<Rate3> listMachineOneAndMutilOkRate(@Param(Constants.WRAPPER) Wrapper<DfLeadDetail> wrapper);


    @Select("select item.* " +
            "from df_lead_check_item_infos item " +
            "left join df_lead_detail det on item.check_id = det.id " +
            "${ew.customSqlSegment} ")
    List<DfLeadCheckItemInfos> listItemInfosJoinDetail(@Param(Constants.WRAPPER) Wrapper<DfLeadDetail> wrapper);

    @Select("select DATE_FORMAT(DATE_SUB(check_time, INTERVAL 7 HOUR),'%m-%d') str1, " +
            "FORMAT(sum(if(check_type = 1 and result = 'OK', 1, 0))/count(0),2) dou1," +
            "FORMAT(sum(if(result = 'OK', 1, 0))/count(0),2) dou2 " +
            "from df_lead_detail " +
            "${ew.customSqlSegment} " +
            "group by str1")
	List<Rate3> listDateOneAndMutilOkRate(@Param(Constants.WRAPPER) QueryWrapper<DfLeadDetail> qw);
}
