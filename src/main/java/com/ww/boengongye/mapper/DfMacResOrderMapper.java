package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfControlStandardConfig;
import com.ww.boengongye.entity.DfMacResOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 机台-工单关系 Mapper 接口
 * </p>
 *
 * @author zhao
 * @since 2023-03-12
 */
public interface DfMacResOrderMapper extends BaseMapper<DfMacResOrder> {

    @Select("select distinct  mac.process_code as process_name , res.create_time, mac.code as machine_code, mac.person_name  as responsible_man " +
            "from df_mac_res_order res  " +
            "left join df_work_order wo on wo.work_order_code = res.work_order_code " +
            "left join df_plain_data pd on wo.id = pd.work_order_id " +
            "left join df_machine mac on mac.code = res.machine_code " +
            "${ew.customSqlSegment} " +
            "order by create_time")
    List<DfMacResOrder> listAllJoinWorkOrder(@Param(Constants.WRAPPER) Wrapper<DfMacResOrder> wrapper);

}
