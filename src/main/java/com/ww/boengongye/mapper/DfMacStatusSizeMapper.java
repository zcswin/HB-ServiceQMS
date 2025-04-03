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
 *  Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2023-03-11
 */
public interface DfMacStatusSizeMapper extends BaseMapper<DfMacStatusSize> {


    @Select("select d.MachineCode,d.StatusID_Cur,d.pub_time,s.name as statusName,s.color as statusColor" +
            ",p.position_x as positionX,p.position_y as positionY,p.position_z as positionZ" +
            ",mac.process_code as process_name,p.mac_type,mac.person_name,mac.dev_ip,mac.name as unitType from " +
            "   df_mac_status_size as d left join df_status_code as s on d.StatusID_Cur=s.status_code " +
            " join df_mac_model_position as p on d.MachineCode=p.MachineCode " +
//            " join df_process ps on p.area=ps.id " +
            " left join df_machine mac on d.MachineCode=mac.code  " +
            " order by d.MachineCode asc")
    List<DfMacStatusSize> listStatus();
//    @Select("select d.MachineCode,d.StatusID_Cur,d.pub_time,s.name as statusName,s.color as statusColor,p.position_x as positionX,p.position_y as positionY,p.position_z as positionZ,ps.process_name,p.mac_type from " +
//            "   df_mac_status_size as d left join df_status_code as s on d.StatusID_Cur=s.status_code join df_mac_model_position as p on d.MachineCode=p.MachineCode join df_process ps on p.area=ps.id order by d.MachineCode asc")


    @Select("select p.MachineCode" +
            ",p.position_x as positionX,p.position_y as positionY,p.position_z as positionZ" +
            ",mac.process_code as process_name,p.mac_type,mac.person_name,mac.dev_ip,mac.name as unitType,mac.project from " +
            "  df_mac_model_position as p  " +
            " left join df_machine mac on p.MachineCode=mac.code  ${ew.customSqlSegment} " +
            " order by p.MachineCode asc")
    List<DfMacStatusSize> listStatus2(@Param(Constants.WRAPPER) Wrapper<DfMacStatusSize> wrapper);





    @Select("select '全部' as statusName, count(id) as count, null as statusColor from `df_mac_status_size` " +
            "union " +
            "select  sta2.name as statusName, count(mac.StatusID_Cur) as count, sta2.color as statusColor " +
            "from df_status_code sta " +
            "left join df_status_code sta2 on sta.parent_status_id = sta2.status_code " +
            "left join df_mac_status_size mac on mac.StatusID_Cur = sta.status_code " +
            "group by sta2.name, sta2.color ")
    List<DfMacStatusSize> countByStatus();


    @Select("SELECT s.*, c.name as statusName,mac.process_code as processName FROM df_mac_status_size s " +
            "left join df_status_code c on s.StatusID_Cur = c.status_code " +
            "left join df_machine mac on s.MachineCode = mac.code " +
            "${ew.customSqlSegment}")
    List<DfMacStatusSize> listJoinCode(@Param(Constants.WRAPPER) Wrapper<DfMacStatusSize> wrapper);


    @Select("select \n" +
            "dmss.MachineCode str1\n" +
            ",dmss.StatusID_Cur inte1\n" +
            ",TIMESTAMPDIFF(SECOND,dmss.change_time,now()) inte2\n" +
            "from df_mac_status_size dmss \n" +
            " ${ew.customSqlSegment} " +
            "order by dmss.MachineCode asc")
    List<Rate3> getProcessMacStatusList(@Param(Constants.WRAPPER)Wrapper<DfMacStatusSize> wrapper);

    @Select("select\n" +
            "dsd.test_time str1\n" +
            ",dsd.machine_status str2\n" +
            ",dsd.status str3\n" +
            "from df_size_detail dsd \n" +
            " ${ew.customSqlSegment} " +
            "order by dsd.test_time desc ")
    List<Rate3> getMacStatusInfoList(@Param(Constants.WRAPPER)Wrapper<DfSizeDetail>wrapper);

    @Select("with mac_sort as(\n" +
            "\tselect \n" +
            "\tdsmd.*\n" +
            "\t,row_number()over(partition by dsmd.machine_code order by dsmd.check_time desc) num \n" +
            "\tfrom df_size_mac_duration dsmd \n" +
            " ${ew.customSqlSegment} " +
            ")\n" +
            ",mac_before as(\n" +
            "\tselect \n" +
            "\tmac_sort.machine_code\n" +
            "\t,sum(if(mac_sort.status=2,mac_sort.duration_time,0)) beforeTime\n" +
            "\tfrom mac_sort\n" +
            "\tgroup by mac_sort.machine_code\n" +
            ")\n" +
            ",mac_now as(\n" +
            "\tselect \n" +
            "\tmac_sort.machine_code\n" +
            "\t,if(mac_sort.after_status=2,TIMESTAMPDIFF(SECOND,mac_sort.check_time,now()),0) nowTime\n" +
            "\tfrom mac_sort\n" +
            "\twhere mac_sort.num = 1\n" +
            ")\n" +
            "select \n" +
            "mac_before.machine_code str1\n" +
            ",mac_before.beforeTime + mac_now.nowTime inte1\n" +
            "from mac_before \n" +
            "left join mac_now\n" +
            "on mac_before.machine_code = mac_now.machine_code\n" +
            "order by inte1 desc ")
    List<Rate3> getProcessMacNormalTime(@Param(Constants.WRAPPER)Wrapper<DfSizeMacDuration>wrapper);

    @Select("with all_process as (\n" +
            "\tselect dp.process_name process\n" +
            "\tfrom df_process dp \n" +
            ")\n" +
            ",count_num as(\n" +
            "\tselect \n" +
            "\tdsd.process\n" +
            "\t,dsd.machine_code\n" +
            "\t,count(0) \n" +
            "\tfrom df_size_detail dsd \n" +
            " ${ew_2.customSqlSegment} " +
            "\tgroup by dsd.process,dsd.machine_code \n" +
            ")\n" +
            ",mac_num as (\n" +
            "\tselect \n" +
            "\tcount_num.process\n" +
            "\t,count(0) total\n" +
            "\tfrom count_num\n" +
            "\tgroup by count_num.process\n" +
            ")\n" +
            ",mac_status as (\n" +
            "\tselect \n" +
            "\tdsmd.process\n" +
            "\t,sum(if(dsmd.status = 2,dsmd.duration_time,0))/sum(if(dsmd.status = 2 or dsmd.status = 10 or dsmd.status = 3,dsmd.duration_time,0)) dou1  \n" +
            "\t,sum(if(dsmd.status = 10,dsmd.duration_time,0))/sum(if(dsmd.status = 2 or dsmd.status = 10 or dsmd.status = 3,dsmd.duration_time,0)) dou2\n" +
            "\t,sum(if(dsmd.status = 3,dsmd.duration_time,0))/sum(if(dsmd.status = 2 or dsmd.status = 10 or dsmd.status = 3,dsmd.duration_time,0)) dou3\n" +
            "\tfrom df_size_mac_duration dsmd \n " +
            " ${ew.customSqlSegment} " +
            "\tgroup by dsmd.process \n" +
            ")\n" +
            "select \n" +
            "all_process.process str1\n" +
            ",if(mac_status.dou1 is null,0,mac_status.dou1) dou1\n" +
            ",if(mac_status.dou2 is null,0,mac_status.dou2) dou2\n" +
            ",if(mac_status.dou3 is null,0,mac_status.dou3) dou3\n" +
            ",if(mac_num.total is null,0,mac_num.total) inte1\n" +
            "from all_process\n" +
            "left join mac_status\n" +
            "on mac_status.process = all_process.process\n" +
            "left join mac_num\n" +
            "on mac_num.process = all_process.process")
    List<Rate3> countSizeMacDurationInfoList(@Param(Constants.WRAPPER)Wrapper<DfSizeMacDuration> wrapper1,@Param("ew_2")Wrapper<DfSizeDetail> wrapper2);

}
