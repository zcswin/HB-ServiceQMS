package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfMacStatusAppearance;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ww.boengongye.entity.DfMacStatusDetail;
import com.ww.boengongye.entity.DfMacStatusSize;
import com.ww.boengongye.entity.DfQmsIpqcWaigTotal;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2023-03-13
 */
public interface DfMacStatusAppearanceMapper extends BaseMapper<DfMacStatusAppearance> {

    @Select("select d.MachineCode,d.StatusID_Cur,d.pub_time,s.name as statusName,s.color as statusColor" +
            ",p.position_x as positionX,p.position_y as positionY,p.position_z as positionZ" +
            ",mac.process_code as process_name,p.mac_type,mac.person_name,mac.dev_ip,mac.name as unitType ,mac.project from " +
            "   df_mac_status_appearance as d left join df_status_code as s on d.StatusID_Cur=s.status_code " +
            " join df_mac_model_position as p on d.MachineCode=p.MachineCode " +
//            " join df_process ps on p.area=ps.id " +
            " left join df_machine mac on d.MachineCode=mac.code ${ew.customSqlSegment} " +
            " order by d.MachineCode asc")
    List<DfMacStatusAppearance> listStatus(@Param(Constants.WRAPPER) Wrapper<DfMacStatusAppearance> wrapper);

@Select("select t.* from (\tselect  *\n" +
        "from df_qms_ipqc_waig_total t\n" +
        "where not exists (\n" +
        "\tselect 1\n" +
        "\tfrom df_qms_ipqc_waig_total\n" +
        "\twhere f_mac = t.f_mac\n" +
        "\tand f_time > t.f_time\n" +
        ")) as t   ${ew.customSqlSegment}")
List<DfQmsIpqcWaigTotal> preparationTimeout(@Param(Constants.WRAPPER) Wrapper<DfQmsIpqcWaigTotal> wrapper);

    @Select("select '全部' as statusName, count(id) as count, null as statusColor from `df_mac_status_appearance` " +
            "union " +
            "select  sta2.name as statusName, count(mac.StatusID_Cur) as count, sta2.color as statusColor " +
            "from df_status_code sta " +
            "left join df_status_code sta2 on sta.parent_status_id = sta2.status_code " +
            "left join df_mac_status_appearance mac on mac.StatusID_Cur = sta.status_code " +
            "group by sta2.name, sta2.color ")
    List<DfMacStatusAppearance> countByStatus();
}
