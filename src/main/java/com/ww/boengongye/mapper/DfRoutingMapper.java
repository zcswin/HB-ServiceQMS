package com.ww.boengongye.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.boengongye.entity.DfRouting;
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
 * @since 2022-09-29
 */
public interface DfRoutingMapper extends BaseMapper<DfRouting> {

    @Select("SELECT rou.*, pro.process_name 'processName', rel.sort 'order' FROM `df_routing` rou " +
            "left join df_routing_relation_process rel on rou.id = rel.routing_id " +
            "left join df_process pro on rel.process_id = pro.id ${ew.customSqlSegment}")
    List<DfRouting> listJoinProcess(@Param(Constants.WRAPPER) Wrapper<DfRouting> wrapper);
}
