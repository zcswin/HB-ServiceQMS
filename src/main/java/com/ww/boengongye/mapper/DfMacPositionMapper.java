package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfControlStandardConfig;
import com.ww.boengongye.entity.DfMacPosition;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ww.boengongye.entity.DfMacResOrder;
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
public interface DfMacPositionMapper extends BaseMapper<DfMacPosition> {

    @Select("select DISTINCT mac.* " +
            "FROM `df_mac_res_order` res " +
            "left join df_mac_position mac on res.mac_mod_id = mac.id " +
            "left join df_work_order ord on res.work_order_id = ord.id " +
            "left join df_plain_data pla on pla.work_order_id = ord.id " +
            "${ew.customSqlSegment}")
    List<DfMacPosition> listByWorkOrder(@Param(Constants.WRAPPER) Wrapper<DfMacResOrder> wrapper);
}
