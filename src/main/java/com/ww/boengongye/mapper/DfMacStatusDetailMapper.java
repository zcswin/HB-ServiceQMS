package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfMacStatus;
import com.ww.boengongye.entity.DfMacStatusDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ww.boengongye.entity.DfMacYieldData;
import org.apache.ibatis.annotations.Mapper;
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
@Mapper
public interface DfMacStatusDetailMapper extends BaseMapper<DfMacStatusDetail> {

    @Select("select t.id from ( " +
            "select *, ROW_NUMBER() OVER (PARTITION BY MachineCode order BY pub_time desc) as 'rank' from df_mac_status_detail) t " +
            "where t.rank > 20 ")
    List<Integer> deleteTimeOut();

    @Select("select det.* , scurdet.name as CurStatusDetail ,scur.name as CurStatus,  spredet.name as PreStatusDetail, spre.name as PreStatus, scur.color as curStatusColor " +
            "from df_mac_status_detail det " +
            "left join df_status_code scurdet on det.StatusID_Cur = scurdet.status_code " +
            "left join df_status_code scur on scurdet.parent_status_id = scur.id " +
            "left join df_status_code spredet on det.StatusID_Pre = spredet.status_code " +
            "left join df_status_code spre on spredet.parent_status_id = spre.id " +
            "where (MachineCode, create_time) in  " +
            "(SELECT MachineCode, Max(create_time) " +
            "FROM `df_mac_status_detail` " +
            "where MachineCode != '' " +
            "group by MachineCode) " +
            "and scur.name != '告警' " +
            "order by det.create_time desc " +
            "limit 3")
    List<DfMacStatusDetail> listNormalStatus();

    @Select("select det.* , scurdet.name as CurStatusDetail ,scur.name as CurStatus,  spredet.name as PreStatusDetail, spre.name as PreStatus , scur.color as curStatusColor " +
            "from df_mac_status_detail det " +
            "left join df_status_code scurdet on det.StatusID_Cur = scurdet.status_code " +
            "left join df_status_code scur on scurdet.parent_status_id = scur.id " +
            "left join df_status_code spredet on det.StatusID_Pre = spredet.status_code " +
            "left join df_status_code spre on spredet.parent_status_id = spre.id " +
            "where (MachineCode, create_time) in  " +
            "(SELECT MachineCode, Max(create_time) " +
            "FROM `df_mac_status_detail` " +
            "where MachineCode != '' " +
            "group by MachineCode) " +
            "and scur.name = '告警' " +
            "order by det.create_time desc " +
            "limit 3")
    List<DfMacStatusDetail> listWarningStatus();

    @Select("SELECT distinct mac.MachineCode, stadet.StatusID_Cur, stadet.create_time " +
            "FROM `df_mac_model_position` mac " +
            "left join df_mac_status_detail stadet on mac.MachineCode = stadet.MachineCode " +
            "where ((stadet.MachineCode, stadet.create_time) in " +
            "(select MachineCode, max(create_time) " +
            "from df_mac_status_detail " +
            "group by MachineCode) or stadet.StatusID_Cur is null) " +
            "and ${ew.sqlSegment}")
    List<DfMacStatusDetail> listInsertMac(@Param(Constants.WRAPPER) Wrapper<DfMacStatusDetail> wrapper);



    @Select("SELECT s.*, c.name as statusName FROM df_mac_status_detail s " +
            "left join df_status_code c on s.StatusID_Cur = c.status_code " +

            "${ew.customSqlSegment}")
    List<DfMacStatusDetail> listJoinCode(@Param(Constants.WRAPPER) Wrapper<DfMacStatusDetail> wrapper);

}
