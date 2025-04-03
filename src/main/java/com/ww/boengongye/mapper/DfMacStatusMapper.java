package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfMacStatus;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ww.boengongye.entity.DfMacStatusSize;
import com.ww.boengongye.entity.DfProcess;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2022-08-04
 */
public interface DfMacStatusMapper extends BaseMapper<DfMacStatus> {


    @Select("select d.MachineCode,d.StatusID_Cur,d.pub_time,s.name as statusName,s.color as statusColor" +
            ",p.position_x as positionX,p.position_y as positionY,p.position_z as positionZ" +
            ",mac.process_code as process_name,p.mac_type,mac.person_name,mac.dev_ip,mac.name as unitType," +
            " cp.name as statusPreName,d.StatusStep from " +
            "   df_mac_status as d left join df_status_code as s on d.StatusID_Cur=s.status_code " +
            " join df_mac_model_position as p on d.MachineCode=p.MachineCode " +
//            " join df_process ps on p.area=ps.id " +
            " left join df_machine mac on d.MachineCode=mac.code " +
            "left join df_status_code cp on d.StatusID_Pre = cp.status_code   ${ew.customSqlSegment}" +
            " order by d.MachineCode asc")
    List<DfMacStatus> listStatus(@Param(Constants.WRAPPER) Wrapper<DfMacStatus> wrapper);

    @Select("select '全部' as statusName, count(id) as count, null as statusColor from `df_mac_status` " +
            "union " +
            "select  sta2.name as statusName, count(mac.StatusID_Cur) as count, sta2.color as statusColor " +
            "from df_status_code sta " +
            "left join df_status_code sta2 on sta.parent_status_id = sta2.status_code " +
            "left join df_mac_status mac on mac.StatusID_Cur = sta.status_code " +
            "group by sta2.name, sta2.color ")
    List<DfMacStatus> countByStatus();


    @Select("SELECT s.*, c.name as statusName,cp.name as statusPreName,mac.process_code as processName FROM df_mac_status s " +
            " join df_status_code c on s.StatusID_Cur = c.status_code " +
            "left join df_status_code cp on s.StatusID_Pre = cp.status_code " +
            "left join df_machine mac on s.MachineCode = mac.code " +
            "${ew.customSqlSegment}")
    List<DfMacStatus> listJoinCode(@Param(Constants.WRAPPER) Wrapper<DfMacStatus> wrapper);

}
