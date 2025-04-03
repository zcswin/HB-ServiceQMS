package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfProcess;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 工序 Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2022-09-19
 */
public interface DfProcessMapper extends BaseMapper<DfProcess> {
    @Select("select * from df_process  d join (select process_id from df_routing_relation_process where routing_id =#{id}) as p where d.id=p.process_id ")
    List<DfProcess> listByRouting(@Param("id")int id);

    @Select("SELECT pro.*, fac.factory_name, sec.name as 'section_name', sta.name as 'workstation_name' FROM df_process pro " +
            "left join df_factory fac on pro.factory_id = fac.id " +
            "left join df_workshop_section sec on pro.section_id = sec.id " +
            "left join df_workstation sta on pro.workstation_id = sta.id " +
            "${ew.customSqlSegment}")
    IPage<DfProcess> listJoinIds(IPage<DfProcess> page, @Param(Constants.WRAPPER) Wrapper<DfProcess> wrapper);

    @Select("select id, max(StatusID_Cur) as processStatus " +
            "from " +
            "(SELECT  pro.id, macsta.StatusID_Cur " +
            "FROM `df_mac_model_position` mac " +
            "left join df_process pro on mac.area = pro.id " +
            "left join df_mac_status macsta on mac.MachineCode = macsta.MachineCode " +
            "group by pro.id, macsta.StatusID_Cur) t " +
            "group by t.id ")
    List<DfProcess> listMacProcessStatus();


    @Select("select DISTINCT( process_name) AS process_name,sort,project FROM df_process " +
            "${ew.customSqlSegment} " )
    List<DfProcess> listDfProcess(@Param(Constants.WRAPPER) Wrapper<DfProcess> wrapper);
}
