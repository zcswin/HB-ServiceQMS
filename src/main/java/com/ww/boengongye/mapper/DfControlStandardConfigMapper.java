package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfControlStandardConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ww.boengongye.entity.ProcessConfig;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 管控标准配置 Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2022-10-09
 */
public interface DfControlStandardConfigMapper extends BaseMapper<DfControlStandardConfig> {

    @Select("select d.create_time ,d.standard_value ,d.detail ,d.data_classify ,r.routing_name ,f.factory_name ,w.name as work_station_name ,ws.name as workshop_section_name from df_control_standard_config d " +
            " join df_routing r on d.routing_id = r.id left join df_factory f on r.factory_code =f.factory_code " +
            " left join df_workstation w on r.station_code=w.code left join df_workshop_section ws on r.section_code=ws.code where d.routing_id=#{id}")
    List<DfControlStandardConfig> listByExport(@Param("id") int id);

    @Select("select d.*,s.data_status,s.id as statusId from df_control_standard_config d left join df_control_standard_status s on d.id =s.control_standrad_id and d.routing_id=s.routing_id and s.batch_id=#{batchId} ${ew.customSqlSegment}  ")
    IPage<DfControlStandardConfig> listByJoinPage(IPage<DfControlStandardConfig> page, @Param(Constants.WRAPPER) Wrapper<DfControlStandardConfig> wrapper,@Param("batchId") String batchId);

    @Select("select  DISTINCT(data_type) data_type from df_control_standard_config where data_type is not null ")
    List<DfControlStandardConfig> listDataType();
}
